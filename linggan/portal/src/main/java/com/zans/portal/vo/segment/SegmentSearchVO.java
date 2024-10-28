package com.zans.portal.vo.segment;

import com.zans.base.vo.BasePage;
import lombok.Data;

/**
 * @author xv
 * @since 2020/6/9 19:31
 */
@Data
public class SegmentSearchVO extends BasePage {

    private String ip;

    private String name;

    private Integer area;
}
