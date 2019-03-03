package lifecycle.model;

/**
 * Desc:定义消息结构
 * Created by sun.tao on 2019/2/22
 */
public class Message {
    //定义消息类型:系统级消息 和 用户级消息
    public final static String TYPE_OF_SYSTEM="SYSTEM";
    public final static String TYPE_OF_USER ="USER";

    //消息标识符，用于标识
    private String ak;
    //消息id
    private int id;
    //消息
    private String content;
    //消息类型
    String type;
    //发送者
    private int senderId;
    //接收者
    private int receiverId;

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }



    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
}
