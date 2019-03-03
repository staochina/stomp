package lifecycle.config;

import lifecycle.Interceptor.HandleShakeInterceptors;
import lifecycle.Interceptor.HttpSessionIdHandshakeInterceptor;
import lifecycle.Interceptor.PresenceChannelInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * "/message-service"被注册为STOMP端点，对外暴露，客户端通过该路径接入WebSocket服务
         */
        logger.debug("---注册端点......");
        registry.addEndpoint("/message-service").setAllowedOrigins("*")
                .withSockJS().setInterceptors(new HandleShakeInterceptors());
        /*registry.addEndpoint("/message-service").setAllowedOrigins("*")
                .setHandshakeHandler(new STOMPHandshakeHandler()).
                addInterceptors(new HandleShakeInterceptors()).withSockJS();*/
    }

    /*
     * 配置消息代理
     * 用户可以订阅来自"/topic"和"/user"的消息，
     * 在Controller中，可通过@SendTo注解指明发送目标，这样服务器就可以将消息发送到订阅相关消息的客户端
     * 使用topic来达到群发效果，使用user进行个人频道消息接收
     * 客户端发送过来的消息，需要以"/app"为前缀，再经过Broker转发给响应的Controller
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        logger.debug("---configureMessageBroker()");
        //设置客户端发送消息前缀；设置客户个人频道
        registry.setApplicationDestinationPrefixes("/app").setUserDestinationPrefix("/user/");
        //设置客户端接收广播主题前缀
        registry.enableSimpleBroker("/topic","/queue");


    }

    /**
     * 消息传输设置
     * @param registry
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(8192) //设置消息字节数大小
                .setSendBufferSizeLimit(8192)//设置消息缓存大小
                .setSendTimeLimit(10000); //设置消息发送时间限制毫秒
    }

    /**
     * 输入通道参数设置
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(4) //设置消息输入通道的线程池线程数
                .maxPoolSize(8)//最大线程数
                .keepAliveSeconds(60);//线程活动时间
        registration.setInterceptors(new PresenceChannelInterceptor());
    }

    /**
     * 输出通道参数设置
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(4).maxPoolSize(8);
    }
}
