package com.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Desc:
 * Created by sun.tao on 2019/2/21
 */
public class JWTTokenUtil {
    private static Logger logger = LoggerFactory.getLogger(JWTTokenUtil.class);
    //设置密钥
    @Value("${auth.jwt.signKey}")
    private static String securitKey="lifecycle";
    //设置token过期时间 6 秒
    @Value("${auth.timeOut}")
    private static long timeOut =6000;

    /**
     * 获取token
     * @param user { "id":1 , "name":"用户名" }
     * @return token
     */
    public static String getToken(JSONObject user) {
        String token="";
        token= JWT.create().withAudience(user.toString())
                .sign(Algorithm.HMAC256(securitKey));
        return token;
    }

    /**
     * 判断是否token值看是否登录成功
     *
     * @return
     */
    public static String isLogin(String jwtStr) {
        return null;
    }

    public static void main(String[] args) {
        JSONObject user = new JSONObject().put("id",1).put("name","root");
        String token = getToken(user);
        logger.debug(token);
        logger.debug("getHeader:"+JWT.decode(token).getHeader());
        logger.debug("getPayload:"+ JWT.decode(token).getPayload());
        logger.debug("getSignature:"+ JWT.decode(token).getSignature());
        logger.debug("getToken:"+ JWT.decode(token).getToken());
    }
}
