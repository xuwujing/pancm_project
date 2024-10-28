package com.zans.portal.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "t_portall")
public class TPortall implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String company;

    @Column(name = "port_cnt")
    private Integer portCnt;

    @Column(name = "open_port")
    private String openPort;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * @return port_cnt
     */
    public Integer getPortCnt() {
        return portCnt;
    }

    /**
     * @param portCnt
     */
    public void setPortCnt(Integer portCnt) {
        this.portCnt = portCnt;
    }

    /**
     * @return open_port
     */
    public String getOpenPort() {
        return openPort;
    }

    /**
     * @param openPort
     */
    public void setOpenPort(String openPort) {
        this.openPort = openPort == null ? null : openPort.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", company=").append(company);
        sb.append(", portCnt=").append(portCnt);
        sb.append(", openPort=").append(openPort);
        sb.append("]");
        return sb.toString();
    }
}