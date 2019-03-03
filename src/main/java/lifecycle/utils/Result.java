package lifecycle.utils;

import org.json.JSONObject;

/**
 * Desc: 请求结果数据封装
 * {
 *     unique: 结果代码
 *     code: 状态码
 *     message: 状态描述
 *     data: 数据结果
 * }
 * Created by sun.tao on 2019/2/20
 */
public class Result {
    //结果代码,用于追踪请求代码,默认使用时间
    private String unique;
    private String message;
    private int code;
    private JSONObject data;

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public Result putDataValue(JSONObject data) {
        this.data =data;
        return this;
    }

    /**
     * Desc: 创建请求结果实例
     * @param code
     * @param message
     */
    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Desc: 组装带有返回数据的对象
     * @param data
     */
    public Result(JSONObject data){
        setOkStatus();
        this.data = data;
    }

    /**
     * 正常的应答状态
     * @return
     */
    public static Result ok() {
        return new Result(200, "Ok");
    }

    private void setOkStatus(){
        this.code = 200;
        this.message ="Ok";
    }

    public static Result notFound() {
        return new Result(404, "Not Found");
    }

    public static Result badRequest() {
        return new Result(400, "Bad Request");
    }

    public static Result forbidden() {
        return new Result(403, "Forbidden");
    }

    public static Result unauthorized() {
        return new Result(401, "unauthorized");
    }

    public static Result serverInternalError() {
        return new Result(500, "Server Internal Error");
    }
}