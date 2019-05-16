package lifecycle.model;

/**
 * Desc:
 * Created by sun.tao on 2019/5/6
 */
public class CallBackMessage {
    //消息id
    private int id;
    private int toId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }
}
