package lifecycle.task;

import lifecycle.dao.impl.MessageDao;
import lifecycle.model.FairMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Desc:
 * Created by sun.tao on 2019/5/9
 */
@Component
public class MessageTask {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageDao messageDao;

    /**
     * 发送常驻消息，保证用户一定能收到
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void taskStaticMessage() {
        List<FairMessage> list = null;
        logger.debug("---- This is taskStaticMessage !"+new Date());
        //将所有就绪状态的常驻信息置为正在发送状态
        list = messageDao.findMessages(FairMessage.STATUS_READY,FairMessage.TYPE_STATIC);
        logger.debug("---- find read,static list: "+Arrays.toString( list.toArray()));
        if(list.size()> 0){
            for(FairMessage fairMessage :list){
                messageDao.updateMessage(fairMessage.getId(),fairMessage.getToId(),FairMessage.STATUS_SENDING);
            }
        }
        //将所有发送状态的常驻信息置为进行发送
        list = messageDao.findMessages(FairMessage.STATUS_SENDING,FairMessage.TYPE_STATIC);
        logger.debug("---- task will send list: "+Arrays.toString( list.toArray()));
        if( list.size()>0){
            for(FairMessage fairMessage : list){
                simpMessagingTemplate.convertAndSendToUser(String.valueOf(fairMessage.getToId()),"/queue",fairMessage);
            }
        }
    }

    /**
     * 发送即时消息，不需要保证用户收到
     */
    @Scheduled(cron = "0/4 * * * * ?")
    public void taskRealMessage() {
        List<FairMessage> list = null;
        logger.debug("---- This is taskRealMessage !"+new Date());
        //将所有就绪状态的常驻信息置为正在发送状态
        list = messageDao.findMessages(FairMessage.STATUS_READY,FairMessage.TYPE_REALTIME);
        logger.debug("---- find read,realtime list: "+Arrays.toString( list.toArray()));
        if( list.size()>0){
            for(FairMessage fairMessage : list){
                simpMessagingTemplate.convertAndSendToUser(String.valueOf(fairMessage.getToId()),"/queue",fairMessage);
                //此处尝试发送即可
                messageDao.updateMessage(fairMessage.getId(),fairMessage.getToId(),FairMessage.STATUS_SENDING);
            }
        }
    }

}
