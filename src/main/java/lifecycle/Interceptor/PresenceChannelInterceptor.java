package lifecycle.Interceptor;

import lifecycle.auth.PortalPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * Desc:
 * Created by sun.tao on 2019/2/27
 */
@Component
public class PresenceChannelInterceptor implements ChannelInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        logger.debug(" ----  preSend () ");
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        //1. 判断是否首次连接请求
        StompCommand command = accessor.getCommand();
        PortalPrincipal user =null;
        switch (command) {
            case CONNECT:
                logger.debug(" ---  CONNECT ");
                //2. 验证是否登录
                //String portalToken = accessor.getNativeHeader("portalToken").get(0);
                //请求portal，验证tocken，并返回 用户相关信息
                String uidStr = accessor.getFirstNativeHeader("uid");
                String name = accessor.getFirstNativeHeader("name");
                logger.debug("--------- uidStr :" + uidStr);
                int uid = Integer.parseInt(uidStr);
                //Principal user = new UserPrincipal(String.valueOf(uid));
                if (3 == uid) {
                    logger.debug("------- user 3 ...");
                    return null;
                }
                user = new PortalPrincipal(uid, name);
                accessor.setUser(user);
                logger.debug(" ----  设置 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case SUBSCRIBE:
                logger.debug(" ----  SUBSCRIBE  ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case UNSUBSCRIBE:
                logger.debug(" ----  UNSUBSCRIBE  ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case DISCONNECT:
                logger.debug(" ----  DISCONNECT ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case ABORT:
                logger.debug(" ----  ABORT ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case ACK:
                logger.debug(" ----  ACK ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case NACK:
                logger.debug(" ----  NACK ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case SEND:
                logger.debug(" ----  SEND ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case BEGIN:
                logger.debug(" ----  BEGIN ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case ERROR:
                logger.debug(" ----  ERROR ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case STOMP:
                logger.debug(" ----  STOMP ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case MESSAGE:
                logger.debug(" ----  MESSAGE ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            case COMMIT:
                logger.debug(" ----  COMMIT ");
                user =(PortalPrincipal) accessor.getUser();
                logger.debug(" ----  获取 user 对象 :" + ((PortalPrincipal) user).toJSON().toString());
                break;
            default:
                break;

        }
        //不是首次连接，已经成功登陆
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        logger.debug(" ----  postSend () sent :" + sent);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        logger.debug(" ----  afterSendCompletion () sent :" + sent);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        logger.debug(" ----  afterSendCompletion ()");
        return true;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        logger.debug(" ----  postReceive ()");
        return message;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        logger.debug(" ----  afterReceiveCompletion ()");
    }
}
