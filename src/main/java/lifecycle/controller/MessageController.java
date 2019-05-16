package lifecycle.controller;

/**
 * Desc:
 * Created by sun.tao on 2019/2/20
 */
import lifecycle.dao.impl.MessageDao;
import lifecycle.model.CallBackMessage;
import lifecycle.model.FairMessage;
import lifecycle.model.AuthPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.List;

@Controller
public class MessageController {
    //SimpMessagingTemplate
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    MessageDao messageDao;

    /**
     * 接收前端传来的消息
     * @param message
     * @throws Exception
     */
    @MessageMapping("/sendMsg")
    public void sendMsg(AuthPrincipal principal, FairMessage message) throws Exception {
        logger.debug("getmessage req :" + HtmlUtils.htmlEscape(message.toString()) + "!");
        int messageId = messageDao.createAndInitMsg(principal.getUid(),new int[]{message.getToId()},message.getType(),message.getContent());
        messageDao.updateMessages(messageId,FairMessage.STATUS_READY);
        List<FairMessage> list = messageDao.findStatusMessagesById(messageId,FairMessage.STATUS_READY);
        for(FairMessage fairMessage :list){
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(fairMessage.getToId()),"/queue",fairMessage);
            messageDao.updateMessage(fairMessage.getId(),fairMessage.getToId(),FairMessage.STATUS_SENDING);
        }
    }

    /**
     * 用户接收到信息后的确认恢复
     * @param principal
     * @param callBackMessage
     * @throws Exception
     */
    @MessageMapping("/callBack")
    public void callBack(AuthPrincipal principal, CallBackMessage callBackMessage) throws Exception {
        int uid = principal.getUid();
        int messageId = callBackMessage.getId();
        int receiverId = callBackMessage.getToId();
        messageDao.updateMessage(messageId,receiverId,FairMessage.STATUS_RECEIVED);
    }

    /**
     * 主动分发任务
     * @param fairMessages
     * @throws Exception
     */
    public void distribuMessage(List<FairMessage> fairMessages) throws Exception{
        for(FairMessage fairMessage: fairMessages){
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(fairMessage.getToId()),"/queue",fairMessage);
        }
    }


}
