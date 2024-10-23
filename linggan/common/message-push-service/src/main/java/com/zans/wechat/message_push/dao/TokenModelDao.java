package com.zans.wechat.message_push.dao;


import com.zans.wechat.message_push.model.TokenModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenModelDao {
    void delete();

    void insert(TokenModel tokenModel);

    String getByAppid(String appid);
}
