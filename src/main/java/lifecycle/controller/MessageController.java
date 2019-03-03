package lifecycle.controller;

/**
 * Desc:
 * Created by sun.tao on 2019/2/20
 */
import lifecycle.model.Message;
import lifecycle.auth.PortalPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class MessageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * 接收前端传来的消息
     * @param message
     * @throws Exception
     */
    @MessageMapping("/sendMsg")
    public void handleMessage(Principal principal, Message message) throws Exception {
        Thread.sleep(2000); // simulated delay
        logger.debug("Hello, " + HtmlUtils.htmlEscape(message.getContent()) + "!");
        if(principal instanceof PortalPrincipal){
            PortalPrincipal portalPrincipal = (PortalPrincipal) principal;

            if( 1 ==portalPrincipal.getUid() ){
                logger.debug("----- message from : "+ portalPrincipal.getUid() );
                Message newMsg = new Message();
                newMsg.setContent( HtmlUtils.htmlEscape(message.getContent()));
                newMsg.setSenderId(portalPrincipal.getUid());
                if(0 == message.getReceiverId()){
                    logger.debug("-----  send to public channel...");
                    simpMessageSendingOperations.convertAndSend("/topic/public",newMsg);
                }
                else {
                    logger.debug("-----  send to private channel... destination user id "+ message.getReceiverId());
                    simpMessageSendingOperations.convertAndSendToUser(String.valueOf(message.getReceiverId()),"/topic",newMsg);
                }
            }
        }

    }



}
