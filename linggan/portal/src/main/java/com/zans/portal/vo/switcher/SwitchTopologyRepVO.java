package com.zans.portal.vo.switcher;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: portal
 * @Description:交换机拓扑图返回值
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/24
 */
@Data
public class SwitchTopologyRepVO implements Serializable {
	private String ip;

	private List<SwitchTopologyRepVO> children;
}
