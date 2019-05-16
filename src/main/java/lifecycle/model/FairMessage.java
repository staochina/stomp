package lifecycle.model;

/**
 * Desc:定义消息结构
 * Created by sun.tao on 2019/2/22
 */
public class FairMessage {
    public static final String MESSAGE_LIST ="MESSAGE_LIST";
    public static final String MESSAGE_RECEIVERS ="MESSAGE_RECEIVERS";
    public static final String STATUS_INIT ="INIT";
    public static final String STATUS_READY ="READY";
    public static final String STATUS_SENDING ="SENDING";
    public static final String STATUS_RECEIVED ="RECEIVED";
    public static final String TYPE_REALTIME ="REALTIME";
    public static final String TYPE_STATIC ="STATIC";

    //消息id
    private int id;

    //发送者
    private int fromId;
    //接收者
    private int toId;

    //消息
    private Object content;

    //消息类型
    private String type;

    //消息状态
    private String status;

    //当前处理节点时间
    private String dealtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDealtime() {
        return dealtime;
    }

    public void setDealtime(String dealtime) {
        this.dealtime = dealtime;
    }

    public String toString(){
        String str = "[ "
                +"id=" + this.id
                +" fromId="+ this.fromId
                +" toId=" + this.toId
                +" type=" +this.type
                +" status="+this.status
                +" dealtime="+this.dealtime
                +" content="+this.content.toString()
        +" ]";
        return str;
    }
}
