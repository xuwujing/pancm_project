package com.zans.portal.dao;

import com.zans.portal.model.TUser;
import com.zans.portal.vo.user.UserReqVO;
import com.zans.portal.vo.user.UserRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TUserMapper extends Mapper<TUser> {

    TUser findUserByName(@Param("userName") String userName);

    TUser findUserByNameExceptId(@Param("userName") String userName, @Param("userId") Integer userId);

    int changeUserEnableStatus(@Param("userId") Integer userId, @Param("status") int status);

    List<UserRespVO> findUserList();

    List<UserRespVO> findUserPageWHJG(@Param("reqVo") UserReqVO reqVO);



    UserRespVO findUserByIdWHJG(@Param("id") Integer id);



    int deleteById(@Param("id") Integer id);

    List<TUser> selectNickName();
}
