package com.zans.mms.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "radius_acct")
public class RadiusAcct implements Serializable {
    @Id
    @Column(name = "rad_acct_id")
    private Long radAcctId;

    /**
     * 来源库，避免主键冲突，radius_server.id
     */
    @Id
    @Column(name = "server_id")
    private Integer serverId;

    @Column(name = "acct_session_id")
    private String acctSessionId;

    @Column(name = "acct_unique_id")
    private String acctUniqueId;

    private String username;

    private String realm;

    @Column(name = "nas_ip_address")
    private String nasIpAddress;

    @Column(name = "nas_port_id")
    private String nasPortId;

    @Column(name = "nas_port_type")
    private String nasPortType;

    @Column(name = "acct_start_time")
    private Date acctStartTime;

    @Column(name = "acct_update_time")
    private Date acctUpdateTime;

    @Column(name = "acct_stop_time")
    private Date acctStopTime;

    @Column(name = "acct_interval")
    private Integer acctInterval;

    @Column(name = "acct_session_time")
    private Integer acctSessionTime;

    @Column(name = "acct_authentic")
    private String acctAuthentic;

    @Column(name = "connect_info_start")
    private String connectInfoStart;

    @Column(name = "connect_info_stop")
    private String connectInfoStop;

    @Column(name = "acct_input_octets")
    private Long acctInputOctets;

    @Column(name = "acct_output_octets")
    private Long acctOutputOctets;

    @Column(name = "called_station_id")
    private String calledStationId;

    @Column(name = "calling_station_id")
    private String callingStationId;

    @Column(name = "acct_terminate_cause")
    private String acctTerminateCause;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "framed_protocol")
    private String framedProtocol;

    @Column(name = "framed_ip_address")
    private String framedIpAddress;

    @Column(name = "framed_ipv6_address")
    private String framedIpv6Address;

    @Column(name = "framed_ipv6_prefix")
    private String framedIpv6Prefix;

    @Column(name = "framed_interface_id")
    private String framedInterfaceId;

    @Column(name = "delegated_ipv6_prefix")
    private String delegatedIpv6Prefix;

    @Column(name = "rad_update_time")
    private Date radUpdateTime;

    private static final long serialVersionUID = 1L;

    /**
     * @return rad_acct_id
     */
    public Long getRadAcctId() {
        return radAcctId;
    }

    /**
     * @param radAcctId
     */
    public void setRadAcctId(Long radAcctId) {
        this.radAcctId = radAcctId;
    }

    /**
     * 获取来源库，避免主键冲突，radius_server.id
     *
     * @return server_id - 来源库，避免主键冲突，radius_server.id
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * 设置来源库，避免主键冲突，radius_server.id
     *
     * @param serverId 来源库，避免主键冲突，radius_server.id
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    /**
     * @return acct_session_id
     */
    public String getAcctSessionId() {
        return acctSessionId;
    }

    /**
     * @param acctSessionId
     */
    public void setAcctSessionId(String acctSessionId) {
        this.acctSessionId = acctSessionId == null ? null : acctSessionId.trim();
    }

    /**
     * @return acct_unique_id
     */
    public String getAcctUniqueId() {
        return acctUniqueId;
    }

    /**
     * @param acctUniqueId
     */
    public void setAcctUniqueId(String acctUniqueId) {
        this.acctUniqueId = acctUniqueId == null ? null : acctUniqueId.trim();
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * @return realm
     */
    public String getRealm() {
        return realm;
    }

    /**
     * @param realm
     */
    public void setRealm(String realm) {
        this.realm = realm == null ? null : realm.trim();
    }

    /**
     * @return nas_ip_address
     */
    public String getNasIpAddress() {
        return nasIpAddress;
    }

    /**
     * @param nasIpAddress
     */
    public void setNasIpAddress(String nasIpAddress) {
        this.nasIpAddress = nasIpAddress == null ? null : nasIpAddress.trim();
    }

    /**
     * @return nas_port_id
     */
    public String getNasPortId() {
        return nasPortId;
    }

    /**
     * @param nasPortId
     */
    public void setNasPortId(String nasPortId) {
        this.nasPortId = nasPortId == null ? null : nasPortId.trim();
    }

    /**
     * @return nas_port_type
     */
    public String getNasPortType() {
        return nasPortType;
    }

    /**
     * @param nasPortType
     */
    public void setNasPortType(String nasPortType) {
        this.nasPortType = nasPortType == null ? null : nasPortType.trim();
    }

    /**
     * @return acct_start_time
     */
    public Date getAcctStartTime() {
        return acctStartTime;
    }

    /**
     * @param acctStartTime
     */
    public void setAcctStartTime(Date acctStartTime) {
        this.acctStartTime = acctStartTime;
    }

    /**
     * @return acct_update_time
     */
    public Date getAcctUpdateTime() {
        return acctUpdateTime;
    }

    /**
     * @param acctUpdateTime
     */
    public void setAcctUpdateTime(Date acctUpdateTime) {
        this.acctUpdateTime = acctUpdateTime;
    }

    /**
     * @return acct_stop_time
     */
    public Date getAcctStopTime() {
        return acctStopTime;
    }

    /**
     * @param acctStopTime
     */
    public void setAcctStopTime(Date acctStopTime) {
        this.acctStopTime = acctStopTime;
    }

    /**
     * @return acct_interval
     */
    public Integer getAcctInterval() {
        return acctInterval;
    }

    /**
     * @param acctInterval
     */
    public void setAcctInterval(Integer acctInterval) {
        this.acctInterval = acctInterval;
    }

    /**
     * @return acct_session_time
     */
    public Integer getAcctSessionTime() {
        return acctSessionTime;
    }

    /**
     * @param acctSessionTime
     */
    public void setAcctSessionTime(Integer acctSessionTime) {
        this.acctSessionTime = acctSessionTime;
    }

    /**
     * @return acct_authentic
     */
    public String getAcctAuthentic() {
        return acctAuthentic;
    }

    /**
     * @param acctAuthentic
     */
    public void setAcctAuthentic(String acctAuthentic) {
        this.acctAuthentic = acctAuthentic == null ? null : acctAuthentic.trim();
    }

    /**
     * @return connect_info_start
     */
    public String getConnectInfoStart() {
        return connectInfoStart;
    }

    /**
     * @param connectInfoStart
     */
    public void setConnectInfoStart(String connectInfoStart) {
        this.connectInfoStart = connectInfoStart == null ? null : connectInfoStart.trim();
    }

    /**
     * @return connect_info_stop
     */
    public String getConnectInfoStop() {
        return connectInfoStop;
    }

    /**
     * @param connectInfoStop
     */
    public void setConnectInfoStop(String connectInfoStop) {
        this.connectInfoStop = connectInfoStop == null ? null : connectInfoStop.trim();
    }

    /**
     * @return acct_input_octets
     */
    public Long getAcctInputOctets() {
        return acctInputOctets;
    }

    /**
     * @param acctInputOctets
     */
    public void setAcctInputOctets(Long acctInputOctets) {
        this.acctInputOctets = acctInputOctets;
    }

    /**
     * @return acct_output_octets
     */
    public Long getAcctOutputOctets() {
        return acctOutputOctets;
    }

    /**
     * @param acctOutputOctets
     */
    public void setAcctOutputOctets(Long acctOutputOctets) {
        this.acctOutputOctets = acctOutputOctets;
    }

    /**
     * @return called_station_id
     */
    public String getCalledStationId() {
        return calledStationId;
    }

    /**
     * @param calledStationId
     */
    public void setCalledStationId(String calledStationId) {
        this.calledStationId = calledStationId == null ? null : calledStationId.trim();
    }

    /**
     * @return calling_station_id
     */
    public String getCallingStationId() {
        return callingStationId;
    }

    /**
     * @param callingStationId
     */
    public void setCallingStationId(String callingStationId) {
        this.callingStationId = callingStationId == null ? null : callingStationId.trim();
    }

    /**
     * @return acct_terminate_cause
     */
    public String getAcctTerminateCause() {
        return acctTerminateCause;
    }

    /**
     * @param acctTerminateCause
     */
    public void setAcctTerminateCause(String acctTerminateCause) {
        this.acctTerminateCause = acctTerminateCause == null ? null : acctTerminateCause.trim();
    }

    /**
     * @return service_type
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    /**
     * @return framed_protocol
     */
    public String getFramedProtocol() {
        return framedProtocol;
    }

    /**
     * @param framedProtocol
     */
    public void setFramedProtocol(String framedProtocol) {
        this.framedProtocol = framedProtocol == null ? null : framedProtocol.trim();
    }

    /**
     * @return framed_ip_address
     */
    public String getFramedIpAddress() {
        return framedIpAddress;
    }

    /**
     * @param framedIpAddress
     */
    public void setFramedIpAddress(String framedIpAddress) {
        this.framedIpAddress = framedIpAddress == null ? null : framedIpAddress.trim();
    }

    /**
     * @return framed_ipv6_address
     */
    public String getFramedIpv6Address() {
        return framedIpv6Address;
    }

    /**
     * @param framedIpv6Address
     */
    public void setFramedIpv6Address(String framedIpv6Address) {
        this.framedIpv6Address = framedIpv6Address == null ? null : framedIpv6Address.trim();
    }

    /**
     * @return framed_ipv6_prefix
     */
    public String getFramedIpv6Prefix() {
        return framedIpv6Prefix;
    }

    /**
     * @param framedIpv6Prefix
     */
    public void setFramedIpv6Prefix(String framedIpv6Prefix) {
        this.framedIpv6Prefix = framedIpv6Prefix == null ? null : framedIpv6Prefix.trim();
    }

    /**
     * @return framed_interface_id
     */
    public String getFramedInterfaceId() {
        return framedInterfaceId;
    }

    /**
     * @param framedInterfaceId
     */
    public void setFramedInterfaceId(String framedInterfaceId) {
        this.framedInterfaceId = framedInterfaceId == null ? null : framedInterfaceId.trim();
    }

    /**
     * @return delegated_ipv6_prefix
     */
    public String getDelegatedIpv6Prefix() {
        return delegatedIpv6Prefix;
    }

    /**
     * @param delegatedIpv6Prefix
     */
    public void setDelegatedIpv6Prefix(String delegatedIpv6Prefix) {
        this.delegatedIpv6Prefix = delegatedIpv6Prefix == null ? null : delegatedIpv6Prefix.trim();
    }

    /**
     * @return rad_update_time
     */
    public Date getRadUpdateTime() {
        return radUpdateTime;
    }

    /**
     * @param radUpdateTime
     */
    public void setRadUpdateTime(Date radUpdateTime) {
        this.radUpdateTime = radUpdateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", radAcctId=").append(radAcctId);
        sb.append(", serverId=").append(serverId);
        sb.append(", acctSessionId=").append(acctSessionId);
        sb.append(", acctUniqueId=").append(acctUniqueId);
        sb.append(", username=").append(username);
        sb.append(", realm=").append(realm);
        sb.append(", nasIpAddress=").append(nasIpAddress);
        sb.append(", nasPortId=").append(nasPortId);
        sb.append(", nasPortType=").append(nasPortType);
        sb.append(", acctStartTime=").append(acctStartTime);
        sb.append(", acctUpdateTime=").append(acctUpdateTime);
        sb.append(", acctStopTime=").append(acctStopTime);
        sb.append(", acctInterval=").append(acctInterval);
        sb.append(", acctSessionTime=").append(acctSessionTime);
        sb.append(", acctAuthentic=").append(acctAuthentic);
        sb.append(", connectInfoStart=").append(connectInfoStart);
        sb.append(", connectInfoStop=").append(connectInfoStop);
        sb.append(", acctInputOctets=").append(acctInputOctets);
        sb.append(", acctOutputOctets=").append(acctOutputOctets);
        sb.append(", calledStationId=").append(calledStationId);
        sb.append(", callingStationId=").append(callingStationId);
        sb.append(", acctTerminateCause=").append(acctTerminateCause);
        sb.append(", serviceType=").append(serviceType);
        sb.append(", framedProtocol=").append(framedProtocol);
        sb.append(", framedIpAddress=").append(framedIpAddress);
        sb.append(", framedIpv6Address=").append(framedIpv6Address);
        sb.append(", framedIpv6Prefix=").append(framedIpv6Prefix);
        sb.append(", framedInterfaceId=").append(framedInterfaceId);
        sb.append(", delegatedIpv6Prefix=").append(delegatedIpv6Prefix);
        sb.append(", radUpdateTime=").append(radUpdateTime);
        sb.append("]");
        return sb.toString();
    }
}
