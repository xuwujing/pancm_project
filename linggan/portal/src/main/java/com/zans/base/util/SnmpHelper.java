package com.zans.base.util;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.zans.base.config.BaseConstants.SEPARATOR_DOT;

/**
 * https://blog.csdn.net/DaveBobo/article/details/51308601
 * @author xv
 * @since 2020/4/17 12:59
 */
@Slf4j
public class SnmpHelper {


    public static final int DEFAULT_VERSION = SnmpConstants.version2c;
    public static final String DEFAULT_PROTOCOL = "udp";
    public static final int DEFAULT_PORT = 161;
    public static final long DEFAULT_TIMEOUT = 3 * 1000L;
    public static final int DEFAULT_RETRY = 3;

    /**
     * 创建对象communityTarget，用于返回target
     *
     * @param ip
     * @param community
     * @return
     */
    public static CommunityTarget createDefault(String ip, String community) {
        Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip
                + "/" + DEFAULT_PORT);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setVersion(DEFAULT_VERSION);
        target.setTimeout(DEFAULT_TIMEOUT);
        target.setRetries(DEFAULT_RETRY);
        return target;
    }

    /**
     * 根据targetOID，获取树形数据
     * @param ip
     * @param community
     * @param inputOid
     */
    public static List<String> snmpWalk(String ip, String community, String inputOid) {
        CommunityTarget target = createDefault(ip, community);
        TransportMapping transport = null;
        Snmp snmp = null;
        List<String> result = new LinkedList<>();
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();

            PDU pdu = new PDU();
            OID targetOid = new OID(inputOid);
            pdu.add(new VariableBinding(targetOid));

            boolean finished = false;
            while (!finished) {
                VariableBinding vb = null;
                ResponseEvent respEvent = snmp.getNext(pdu, target);

                PDU response = respEvent.getResponse();

                if (null == response) {
                    finished = true;
                    break;
                } else {
                    vb = response.get(0);
                }
                // check finish
                finished = checkWalkFinished(targetOid, pdu, vb);
                if (!finished) {
                    String value = vb.getOid() + " = " + vb.getVariable();
                    result.add(value);
                    log.info("{}.{}={}", result.size(), vb.getOid(), vb.getVariable());
                    // Set up the variable binding for the next entry.
                    pdu.setRequestID(new Integer32(0));
                    pdu.set(0, vb);
                } else {
                    snmp.close();
                }
            }
        } catch (Exception e) {
            log.error("SNMP walk Exception: ", e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
        return result;
    }

    private static boolean checkWalkFinished(OID inputOid, PDU pdu,
                                             VariableBinding vb) {
        boolean finished = false;
        if (pdu.getErrorStatus() != 0) {
            System.out.println(pdu.getErrorStatusText());
            finished = true;
        } else if (vb.getOid() == null) {
            finished = true;
        } else if (vb.getOid().size() < inputOid.size()) {
            finished = true;
        } else if (inputOid.leftMostCompare(inputOid.size(), vb.getOid()) != 0) {
            finished = true;
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            finished = true;
        } else if (vb.getOid().compareTo(inputOid) <= 0) {
            finished = true;
        }
        return finished;
    }


    public static void snmpSet(String ip, String community, PDU pdu) {
        CommunityTarget target = createDefault(ip, community);
        TransportMapping transport = null;
        Snmp snmp = null;
        List<String> result = new LinkedList<>();
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();
            sendRequest(snmp, pdu, target);
        } catch (Exception e) {
            log.error("SNMP walk Exception: ", e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
    }

    public static PDU getBlankHolePdu(String mac) {
        String macOid = convertMacToOid(mac);
        if (macOid == null) {
            return null;
        }
        PDU pdu = new PDU();
        pdu.setType(PDU.SET);
        String oid = String.format("1.3.6.1.4.1.2011.5.25.42.2.1.2.1.5.%s.0.1.48", macOid);
        String oid2 = String.format("1.3.6.1.4.1.2011.5.25.42.2.1.2.1.6.%s.0.1.48", macOid);
        pdu.add(new VariableBinding(new OID(oid), new Integer32(3)));
        pdu.add(new VariableBinding(new OID(oid2), new Integer32(4)));
        return pdu;
    }

    public static PDU getUndoBlackHolePdu(String mac) {
        String macOid = convertMacToOid(mac);
        if (macOid == null) {
            return null;
        }
        PDU pdu = new PDU();
        pdu.setType(PDU.SET);
        String oid = String.format("1.3.6.1.4.1.2011.5.25.42.2.1.2.1.6.%s.0.1.48", macOid);
        pdu.add(new VariableBinding(new OID(oid), new Integer32(6)));
        return pdu;
    }

    public static String convertMacToOid(String mac) {
        if (!StringHelper.isSpaceMacValid(mac)) {
            return null;
        }
        String[] array = mac.split(" ");
        List<Object> oidList = new ArrayList<>(array.length);
        for(String part : array) {
            long decNum = Long.parseLong(part, 16);
            oidList.add(decNum);
        }
        return StringHelper.joinCollection(oidList, SEPARATOR_DOT);
    }

    private static void sendRequest(Snmp snmp, PDU pdu, CommunityTarget target)
            throws IOException {
        ResponseEvent responseEvent = snmp.send(pdu, target);
        PDU response = responseEvent.getResponse();
        if (response != null){
            if (response.getErrorStatus() == PDU.noError) {
                List<? extends VariableBinding> vbs = response.getVariableBindings();
                for (VariableBinding vb : vbs) {
                    log.info(vb + " ," + vb.getVariable().getSyntaxString());
                }
            } else {
                log.error("error, {}", response);
            }
        }
    }

    /*根据OID，获取单条消息*/
    public static Boolean snmpGet(String ip, String community, String oid) {

        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            // pdu.add(new VariableBinding(new OID(new int[]
            // {1,3,6,1,2,1,1,2})));
            pdu.add(new VariableBinding(new OID(oid)));

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            System.out.println("-------> 发送PDU <-------");
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            System.out.println("PeerAddress:" + respEvent.getPeerAddress());
            PDU response = respEvent.getResponse();

            if (response == null) {
                System.out.println("response is null, request time out");
                return false;
            } else {

                // Vector<VariableBinding> vbVect =
                // response.getVariableBindings();
                // System.out.println("vb size:" + vbVect.size());
                // if (vbVect.size() == 0) {
                // System.out.println("response vb size is 0 ");
                // } else {
                // VariableBinding vb = vbVect.firstElement();
                // System.out.println(vb.getOid() + " = " + vb.getVariable());
                // }

                System.out.println("response pdu size is " + response.size());
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    System.out.println(vb.getOid() + " = " + vb.getVariable());
                }

            }
            System.out.println("SNMP GET one OID value finished !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SNMP Get Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }

        }
        return true;
    }

    public static void main(String[] args) {
        // snmpwalk  1.3.6.1.4.1.2011.5.25.123.1.17.1
        String ip = "127.0.0.1";
        String community = "public";
        String oidval = "1.3.6.1.2.1.1.6.0";
        String mac = "d8 b0 4c e2 06 15";
        Boolean aBoolean = snmpGet(ip, community, oidval);
        System.out.println(aBoolean);


        //PDU pdu = getUndoBlackHolePdu(mac);
        //snmpSet(ip, community, pdu);

    }

}
