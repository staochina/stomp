package lifecycle.auth;

import org.json.JSONObject;

import java.security.Principal;

/**
 * Desc: 通过portal token 验证
 * Created by sun.tao on 2019/2/22
 */
public class PortalPrincipal implements Principal {
    // 用户名
    private String name;
    //用户id
    private int uid;

    public PortalPrincipal(int uid,String name){
        this.uid = uid;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setUid(int uid){
        this.uid = uid;
    }
    public int getUid(){
        return uid;
    };

    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        obj.put("uid",this.uid);
        obj.put("name",this.name);
        return obj;
    }

}
