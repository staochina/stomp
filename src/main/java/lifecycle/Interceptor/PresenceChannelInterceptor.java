package lifecycle.Interceptor;

import lifecycle.dao.impl.MessageDao;
import lifecycle.model.AuthPrincipal;
import lifecycle.service.AuthService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * Desc:
 * Created by sun.tao on 2019/2/27
 */
@Component
public class PresenceChannelInterceptor implements ChannelInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AuthService authService;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    DataSource dataSource;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        //1. 判断是否首次连接请求
        StompCommand command = accessor.getCommand();
        AuthPrincipal user = null;
        switch (command) {
            case CONNECT:
                logger.debug(" ---  CONNECT ");
                //2. 验证是否登录
                //String portalToken = accessor.getNativeHeader("portalToken").get(0);
                //请求portal，验证tocken，并返回 用户相关信息
                String uidStr = accessor.getFirstNativeHeader("uid");
                String userKey = accessor.getFirstNativeHeader("userKey");
                int uid = Integer.parseInt(uidStr);
                user = new AuthPrincipal();
                user.setUid(Integer.parseInt(uidStr));
                accessor.setUser(user);
                logger.debug(" ----  设置 user 对象 :" + ((AuthPrincipal) user).toJSON().toString());
                break;
            case SUBSCRIBE:
                logger.debug(" ----  SUBSCRIBE  ");
                user = (AuthPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((AuthPrincipal) user).toJSON().toString());
                break;
            case UNSUBSCRIBE:
                logger.debug(" ----  UNSUBSCRIBE  ");
                user = (AuthPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((AuthPrincipal) user).toJSON().toString());
                break;
            case DISCONNECT:
                logger.debug(" ----  DISCONNECT ");
                user = (AuthPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((AuthPrincipal) user).toJSON().toString());
                break;
            case ABORT:
                logger.debug(" ----  ABORT ");

                break;
            case ACK:
                logger.debug(" ----  ACK ");

                break;
            case NACK:
                logger.debug(" ----  NACK ");
                break;
            case SEND:
                logger.debug(" ----  SEND ");
                break;
            case BEGIN:
                logger.debug(" ----  BEGIN ");
                break;
            case ERROR:
                logger.debug(" ----  ERROR ");
                break;
            case STOMP:
                logger.debug(" ----  STOMP ");
                break;
            case MESSAGE:
                logger.debug(" ----  MESSAGE ");
                break;
            case COMMIT:
                logger.debug(" ----  COMMIT ");
                break;
            default:
                break;

        }
        //已经成功登陆
        return message;
    }

    /*@Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        logger.debug(" ----  postSend () sent :" + sent);
        logger.debug("  --  channel ");
    }*/

    /*@Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        logger.debug(" ----  afterSendCompletion () sent :" + sent);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        logger.debug(" ----  preReceive ()");
        return true;
    }*/

    /*@Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        logger.debug(" ----  postReceive ()");
        return message;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        logger.debug(" ----  afterReceiveCompletion ()");
    }*/

    /**
     * 组装查询参数
     *
     * @param userKey 当前用户的sessionkey
     * @param uid     当前用户的id
     * @return
     */
    private JSONObject getAuthParams(String userKey, int uid) {
        JSONObject authParam = new JSONObject();
        authParam.put("cmd", "auth");
        authParam.put("userKey", StringUtils.isEmpty(userKey) ? JSONObject.NULL : userKey);
        authParam.put("uid", uid);
        return authParam;
    }

}
