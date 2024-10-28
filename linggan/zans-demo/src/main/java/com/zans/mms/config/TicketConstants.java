package com.zans.mms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author pancm
 * @Title: portal
 * @Description: 工单相关实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/20
 */
@Slf4j
@Component
public class TicketConstants {



    /**
     *  工单相关状态 start
     */
    public static String MODULE_ISSUE_SOURCE = "issue_source";
    public static String MODULE_ISSUE_TYPE = "issue_type";
    public static String MODULE_ISSUE_LEVEL = "issue_level";
    public static String MODULE_TICKET_STATUS = "ticket_status";
    public static String MODULE_TICKET_TYPE = "ticket_type";
    public static String MODULE_TICKETS_DISPATCH_STATUS = "dispatch_status";
    public static String MODULE_TICKETS_ACCEPT_STATUS = "accept_status";
    public static String MODULE_TICKETS_MAINTENANCE_STATUS = "maintenance_status";
    public static String MODULE_TICKET_OP = "ticket_op";
    public static String MODULE_TICKET_TS = "ticket_ts";

    /**
     *  工单相关状态 end
     */

    /**
     *
     */
    public static String MODULE_BASE_ORG = "base_org";

    /**
     *  工单相关状态
     */
    public static final String PC = "1";
    public static final String APP = "2";

    public static final int IS_COST_NO = 0;
    public static final int IS_COST_YES = 1;

    public static final int TICKET_OP_TYPE = 0;
    public static final int DISPATCH_OP_TYPE = 1;
    public static final int ACCEPT_OP_TYPE = 2;
    public static final int APP_OP_TYPE = 3;


    public static final int TICKET_AGREE_NO = 0;
    public static final int TICKET_AGREE_YES = 1;
    public static final int TICKET_AGREE_RETURN = 3;

    /** 0:待分配
     *  1：检修中
     *    2：待审核 (派工状态 = 待监理审核 ，待业主审核  or 验收单状态 = 待监理审核 ，待业主审核 )
     *    3：待验收（派工状态 = 待业主审核 and 验收单状态 = 待业主审核）
     *   20：已验收
     **/
     public static final int TICKET_STATUS_INIT = 0;
     public static final int TICKET_STATUS_START = 1;
     public static final int TICKET_STATUS_CHECK = 2;
     public static final int TICKET_STATUS_VERIFY = 3;
     public static final int TICKET_STATUS_DRAFT = 19;
     public static final int TICKET_STATUS_SUC = 20;

     /**
      *   1：未添加
      *   2：编辑中
      *   3：待监理审核
      *   4：待业主审核
      *   20：已审核
      **/
     public static final int DISPATCH_STATUS_START = 1;
     public static final int DISPATCH_STATUS_EDIT = 2;
     public static final int DISPATCH_STATUS_SUP_CHECK = 3;
     public static final int DISPATCH_STATUS_OWNER_CHECK = 4;
     public static final int DISPATCH_STATUS_SUC = 20;

     /**
      *   1：未添加
      *   2：编辑中
      *   3：待监理审核
      *   4：待业主审核
      *   20：已审核
      **/
     public static final int ACCEPT_STATUS_START = 1;
     public static final int ACCEPT_STATUS_EDIT = 2;
     public static final int ACCEPT_STATUS_SUP_CHECK = 3;
     public static final int ACCEPT_STATUS_OWNER_CHECK = 4;
     public static final int ACCEPT_STATUS_SUC = 20;

    /**
     *   1：维修中
     *   20：已完成
     **/
    public static final int MAINTENANCE_STATUS_INIT = 0;
    public static final int MAINTENANCE_STATUS_START = 1;
    public static final int MAINTENANCE_STATUS_SUC = 20;


    /**
     *   1：编辑中
     *   20：已完成
     **/
    public static final int EDIT_STATUS_START = 1;
    public static final int EDIT_STATUS_SUC = 20;

    /**
     *   1：故障
     *   2：零星
     **/
    public static final int TICKET_TYPE_ISSUE_STATUS = 1;
    public static final int TICKET_TYPE_SPORADIC_STATUS = 2;



    /**
     * 内场: 0301
     * 业主:0101
     * 监理:0501
     */
    public static final String INSIDE_ROLE = "0301";
    public static final String OUTFIELD_ROLE = "0302";
    public static final String SUPERVISION_ROLE = "0501";
    public static final String OWNER_ROLE = "0101";

    /**
     * 内场: 0
     * 监理:1
     * 业主:2
     */
    public static final int INSIDE_ROLE_TYPE = 0;
    public static final int SUPERVISION_ROLE_TYPE = 1;
    public static final int OWNER_ROLE_TYPE = 2;


    public static final int TICKET_TEMPLATE_CREATE = 11;
    public static final int TICKET_TEMPLATE_DISPATCH_SUBMIT = 12;
    public static final int TICKET_TEMPLATE_ACCEPT_SUBMIT = 13;




    public static  String MSG_FORMAT_GIS = "point(%s %s)";

    /**
     * P1严重
     */
    public static final Integer TICKET_ISSUE_LEVEL_SERIOUS = 1;
    /**
     * P2紧急
     */
    public static final Integer TICKET_ISSUE_LEVEL_URGENT = 2;
    /**
     * P3一般
     */
    public static final Integer TICKET_ISSUE_LEVEL_COMMONLY = 3;
    /**
     * P4轻微
     */
    public static final Integer TICKET_ISSUE_LEVEL_SLIGHT = 4;

}

