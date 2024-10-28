package com.zans.dao;


import com.zans.vo.DbVersionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author beixing
 * @Title: (DbVersion)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 17:54:32
 */
@Mapper
public interface DbVersionDao {


    @Select("SELECT version,remark FROM db_version ORDER BY create_time DESC LIMIT 1 ")
    DbVersionVO queryNewOne();



}

