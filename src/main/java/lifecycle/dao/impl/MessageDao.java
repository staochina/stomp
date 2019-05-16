package lifecycle.dao.impl;

import lifecycle.RowMapperImpl.FairMessageRowMapper;
import lifecycle.model.FairMessage;
import lifecycle.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * Desc:
 * Created by sun.tao on 2019/5/6
 */
@Repository
public class MessageDao {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     *  获取sequence
     * @param sequenceName
     * @return id
     */
    private int getSequences(String sequenceName){
        String sql = "{? = call get_Sequences(?)}";
        Object obj = jdbcTemplate.execute(sql,new CallableStatementCallback(){
            public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                cs.registerOutParameter(1,Types.INTEGER );
                cs.setString(2, sequenceName);
                cs.execute();
                return cs.getInt(1);
            }
        });
        int id=((Integer)obj).intValue();
        return id;
    }

    /**
     * 新增和初始化消息
     * @param fromId
     * @param toIds
     * @param type
     * @param content
     * @return messageId
     */
    @Transactional
    public int createAndInitMsg(int fromId,int[] toIds,String type ,Object content ){
        int messageId =getSequences(FairMessage.MESSAGE_LIST);
        jdbcTemplate.update("insert into message_list (id, ad_org_id, from_id, type, content, ownerid, creationdate )" +
                " values(? , ?, ? , ?, ? , ? , sysdate )",
                new Object[]{ messageId, Constant.AD_ORG_ID, fromId,type,content ,fromId });
        for(int i=0,length = toIds.length;i<length ;i++){
            int itemId = getSequences(FairMessage.MESSAGE_RECEIVERS);
            jdbcTemplate.update("insert into message_receivers ( id, AD_ORG_ID, MESSAGE_ID,TO_ID,STATUS,DEALTIME,OWNERID ,CREATIONDATE )" +
                    " values(? ,? , ?, ?, ? , sysdate, ?, sysdate )",
                    new Object[]{ itemId, Constant.AD_ORG_ID, messageId, toIds[i], FairMessage.STATUS_INIT,fromId });
        }
        return  messageId;
    }


    /**
     *  查找消息
     * @param messageId
     * @param status
     * @return
     */
    @Transactional
    public List<FairMessage> findStatusMessagesById(int messageId,String status){
         List<FairMessage> list = jdbcTemplate.query("select a.id id,a.from_id from_id , a.type type, a.content content, b.to_id to_id, b.status status, b.dealtime dealtime from message_list a ,message_receivers b " +
                        "where a.id = ?  and a.isactive='Y' and a.id=b.message_id and b.status = ? and b.isactive='Y'",
                new Object[]{ messageId,status },new FairMessageRowMapper());
        return list;
    }

    /**
     * 查找消息
     * @param status
     * @return
     */
    @Transactional
    public List<FairMessage> findMessagesByStatus(String status){
        List<FairMessage> list = jdbcTemplate.query("select a.id id,a.from_id from_id, a.type type, a.content content, b.to_id to_id, b.status status, b.dealtime dealtime from message_list a ,message_receivers b " +
                        "where a.isactive='Y' and a.id=b.message_id and b.status = ? and b.isactive='Y'",
                new Object[]{status },new FairMessageRowMapper());
        return list;
    }


    /**
     *  查找消息
     * @param status
     * @param type
     * @return
     */
    @Transactional
    public List<FairMessage> findMessages(String status,String type){
        List<FairMessage> list = jdbcTemplate.query("select a.id id,a.from_id from_id, a.type type, a.content content, b.to_id to_id, b.status status, b.dealtime dealtime from message_list a ,message_receivers b " +
                        "where a.type = ? and a.isactive='Y' and a.id=b.message_id and b.status = ? and b.isactive='Y'",
                new Object[]{type ,status},new FairMessageRowMapper());
        return list;
    }

    /**
     * 更新消息状态
     * @param messageId
     * @param status
     * @return
     */
    @Transactional
    public boolean updateMessages(int messageId ,String status ){
        boolean iSsucceed = false;
        int rowAccount = jdbcTemplate.update("update message_receivers a set a.status = ? ,a.dealtime=sysdate  where a.message_id = (select b.id from message_list b where b.id=? and b.isactive='Y') and a.isactive = 'Y'",
                new Object[]{status,messageId});
        if(rowAccount> 0){
            iSsucceed = true;
        }
        return iSsucceed;
    }

    /**
     *  更新消息状态
     * @param messageId
     * @param toId
     * @param status
     * @return
     */
    @Transactional
    public boolean updateMessage(int messageId, int toId, String status){
        boolean iSsucceed = false;
        int rowAccount = jdbcTemplate.update("update message_receivers a set a.status = ? , a.dealtime=sysdate  where a.message_id = (select b.id from message_list b where b.id=? and b.isactive='Y') and a.to_id= ? and a.isactive = 'Y'",
                new Object[]{status,messageId ,toId});
        if(rowAccount == 1){
            iSsucceed = true;
        }
        return iSsucceed;
    }

}
