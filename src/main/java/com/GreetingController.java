package com;

/**
 * Desc:
 * Created by sun.tao on 2019/2/20
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/com/{uid}")
    //@SendTo("/topic/greetings")
    public void greeting( HelloMessage message,) throws Exception {
        logger.debug(" uid: "+message.getUid());
        Thread.sleep(2000); // simulated delay
        logger.debug("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
        if("1".equals(String.valueOf(message.getUid()))){
            int touid =2;
            logger.debug("send to "+touid);
            simpMessageSendingOperations.convertAndSend("/topic/greetings/"+touid, new Greeting( HtmlUtils.htmlEscape(message.getName()) + "!"));
        }
        if("2".equals(String.valueOf(message.getUid())) ){
            int touid =1;
            logger.debug("send to "+touid);
            simpMessageSendingOperations.convertAndSend("/topic/greetings/"+touid,new Greeting(HtmlUtils.htmlEscape(message.getName()) + "!"));
        }
    }

}
