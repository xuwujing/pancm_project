package com.zans.mms.dao.mms;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.BaseOrg;
import com.zans.mms.vo.baseorg.BaseOrgRepVO;
import com.zans.mms.vo.baseorg.BaseOrgReqVO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface BaseOrgMapper extends Mapper<BaseOrg> {
    List<SelectVO> orgList();

    List<SelectVO> orgTypeList();


    List<SelectVO> queryBaseOrg();

    List<BaseOrgRepVO> queryAll(BaseOrgReqVO baseOrgReqVO);

    BaseOrgRepVO queryByOrgId(String orgId);

    BaseOrgRepVO findOne(BaseOrgReqVO baseOrgReqVO);


}
