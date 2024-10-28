package com.zans.mms.dao.guard;

import com.zans.mms.model.SaleProject;
import com.zans.mms.vo.saleproject.SaleProjectQueryVO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SaleProjectMapper extends Mapper<SaleProject> {
    List<SaleProject> getList(SaleProjectQueryVO vo);

    void deleteById(Long id);

    SaleProject getByProjectId(String projectId);
}