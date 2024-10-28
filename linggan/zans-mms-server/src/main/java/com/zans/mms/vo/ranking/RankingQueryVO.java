package com.zans.mms.vo.ranking;

import com.zans.base.vo.BasePage;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/8
 */
@Data
public class RankingQueryVO extends BasePage implements Serializable {

	private String currentDate;

	private Integer type;

}
