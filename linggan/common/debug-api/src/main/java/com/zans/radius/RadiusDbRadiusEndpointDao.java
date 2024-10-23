package com.zans.radius;

import com.zans.vo.radius.RadiusEndpointVO;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * @author beixing
 * @Title: (RadiusEndpoint)表数据库访问层
 * radius的数据库
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022-01-14 15:55:13
 */
@Qualifier("radiusDataSource")
@Mapper
public interface RadiusDbRadiusEndpointDao {


    @Insert("insert into radius_endpoint(pass,username,access_policy,base_ip, delete_status)" +
            " values('${username}', '${username}', '${accessPolicy}', '${baseIp}', '${deleteStatus}')")
    int insert(RadiusEndpointVO vo);

    @Delete("delete from radius_endpoint where username = #{username}")
    int delete(String username);

    @Update("update radius_endpoint set access_policy=#{accessPolicy}, " +
            "delete_status=#{deleteStatus}, base_ip=#{baseIp} where username=#{username}")
    int update(RadiusEndpointVO vo);

    @Update("update radius_endpoint set access_policy=#{accessPolicy}" +
            " where username=#{username}")
    int resetPolicy(RadiusEndpointVO vo);

    @Select("select 1 from radius_endpoint where  username = #{username}")
    int findOne(String username);




}
