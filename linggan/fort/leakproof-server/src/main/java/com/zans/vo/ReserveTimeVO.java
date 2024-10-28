package com.zans.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveTimeVO {

    private String title;

    private String date;

    /**
     * {
     *           title: '可预约',
     *           date: '2021-06-28',
     *         },
     *         {
     *           title: '预约已满',
     *           date: '2021-06-27',
     *         },
     *         {
     *           title: '已预约',
     *           date: '2021-06-29',
     *         },
     *         {
     *           title: '已预约',
     *           date: '2021-06-20',
     *         }
     */

}
