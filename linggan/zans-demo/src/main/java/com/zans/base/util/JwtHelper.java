package com.zans.base.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zans.base.config.GlobalConstants;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

import static com.zans.base.config.GlobalConstants.*;

@Slf4j
@Component
public class JwtHelper {

    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "kxky#jdk-8u2419524587417$12";

    /**
     * 生成签名,15分钟后过期
     * @param userName
     * @param userId
     * @return
     */
    public String sign(String userName, String userId, String nickName){
        // 过期时间
        Date date = new Date(System.currentTimeMillis() + GlobalConstants.JWT_EXPIRE_TIME);
        // 私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        // 设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        // 附带username和userID生成签名
        return JWT.create().withHeader(header)
                .withClaim(SESSION_KEY_PASSPORT, userName)
                .withClaim(SESSION_KEY_USER_ID, userId)
                .withClaim(SESSION_KEY_NICK_NAME, nickName)
                .withExpiresAt(date).sign(algorithm);
    }
    public String signUser(SysUser sysUser){
        // 过期时间
        Date date = new Date(System.currentTimeMillis() + GlobalConstants.JWT_EXPIRE_TIME);
        // 私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        // 设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        // 附带username和userID生成签名
        return JWT.create().withHeader(header)
                .withClaim(SESSION_KEY_PASSPORT, sysUser.getUserName())
                .withClaim(SESSION_KEY_USER_ID, sysUser.getId().toString())
                .withClaim(SESSION_KEY_NICK_NAME, sysUser.getNickName())
                .withClaim(SESSION_KEY_ORG_ID, sysUser.getMaintainNum())
                .withClaim(SESSION_KEY_AREA_ID_STR, sysUser.getAreaIdStr())
                .withClaim(SESSION_KEY_ROLE_ID, sysUser.getRoleNum())
                .withExpiresAt(date).sign(algorithm);
    }

    public String signApp(String userName, String userId, String nickName){
        // 过期时间
        Date date = new Date(System.currentTimeMillis() + GlobalConstants.APP_JWT_EXPIRE_TIME);
        // 私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        // 设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        // 附带username和userID生成签名
        return JWT.create().withHeader(header)
                .withClaim(SESSION_KEY_PASSPORT, userName)
                .withClaim(SESSION_KEY_USER_ID, userId)
                .withClaim(SESSION_KEY_NICK_NAME, nickName)
                .withExpiresAt(date).sign(algorithm);
    }

    @Cacheable(cacheNames = "JWT_TOKEN_VERITY", key="#token")
    public boolean verity(String token){
        if (token == null) {
            return false;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt != null && jwt.getExpiresAt().getTime() > System.currentTimeMillis()
                    && getClaimAsString(jwt, SESSION_KEY_USER_ID) != null ) {
                return true;
            }
        } catch (TokenExpiredException te) {
            log.error("verify error, The Token has expired#" + token);
        } catch (Exception e) {
            log.error("verify error#" + token, e);
        }
        return false;
    }

    @Cacheable(cacheNames = "JWT_TOKEN_USER", key="#token")
    public UserSession getUserSession(String token) {
        if (token == null) {
            return null;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt == null || jwt.getExpiresAt().getTime() < System.currentTimeMillis()) {
                return null;
            }
            log.info("jwt token#{} -> {}", token, jwt.getExpiresAt());
            String user = getClaimAsString(jwt, SESSION_KEY_USER_ID);
            if (user == null) {
                return null;
            }

            UserSession session = new UserSession();
            session.setUserId(Integer.parseInt(user));
            session.setUserName(getClaimAsString(jwt, SESSION_KEY_PASSPORT));
            session.setNickName(getClaimAsString(jwt, SESSION_KEY_NICK_NAME));
            session.setOrgId(getClaimAsString(jwt, SESSION_KEY_ORG_ID));
            session.setAreaIdStr(getClaimAsString(jwt, SESSION_KEY_AREA_ID_STR));
            session.setRoleId(getClaimAsString(jwt, SESSION_KEY_ROLE_ID));
            return session;
        } catch (Exception e) {
            log.error("getUserId error#{}," ,token, e);
        }
        return null;
    }

    private static String getClaimAsString(DecodedJWT jwt, String key) {
        Claim claim = jwt.getClaim(key);
        return claim == null ? "" : claim.asString();
    }

}
