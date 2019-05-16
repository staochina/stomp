package lifecycle.model;

import org.json.JSONObject;

import java.security.Principal;

/**
 * Desc: 通过portal token 验证
 * Created by sun.tao on 2019/2/22
 */
public class AuthPrincipal implements Principal {
    public static final int GEST_ID = -1;
    public static final String GEST_NAME ="GEST";

    //用户id
    private int uid;
    // 用户名
    private String name;

    public AuthPrincipal(){}

    public AuthPrincipal(int uid, String name){
        this.uid = uid;
        this.name = name;
    }

    /**
     *
     * @param object  { uid , name }
     */
    public AuthPrincipal(JSONObject object){
        this.uid= object.optInt("id",AuthPrincipal.GEST_ID);
        this.name = object.optString("name",AuthPrincipal.GEST_NAME);
    }


    @Override
    public String getName() {
        return String.valueOf(this.uid);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(int uid){
        this.uid = uid;
    }
    public int getUid(){
        return uid;
    };

    /**
     * 当前用户是否是匿名用户
     * 通过当前用户的id是否为 -1
     * @param authPrincipal
     * @return
     */
    public static boolean isGest(AuthPrincipal authPrincipal){
        boolean isGest = true;
        if(authPrincipal.uid != AuthPrincipal.GEST_ID){
            isGest = false;
        }
        return isGest;
    };

    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        obj.put("uid",this.uid);
        obj.put("name",this.name);
        return obj;
    }

}
