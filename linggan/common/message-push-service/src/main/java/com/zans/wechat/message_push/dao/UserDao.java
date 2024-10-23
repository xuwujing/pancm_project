package com.zans.wechat.message_push.dao;

import com.zans.wechat.message_push.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    String getOpenid(String unionid);

    int insertList(@Param("users")List<User> users);

    void insert(User user);

    List<String> getUnionid();
}
