package com.zans.mms.vo.patrol;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "PatrolSchemeQueryVO", description = "")
@Data
public class PatrolSchemeQueryVO extends BasePage implements Serializable {
    private String schemeName;
    private List<String> orgIds;
}
