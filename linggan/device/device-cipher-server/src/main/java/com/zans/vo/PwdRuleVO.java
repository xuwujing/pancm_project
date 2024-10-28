package com.zans.vo;

import lombok.Data;

import java.util.List;

@Data
public class PwdRuleVO {

    private Integer period;

    private List<Integer> charactersType;

    private Integer pwdLength;

}
