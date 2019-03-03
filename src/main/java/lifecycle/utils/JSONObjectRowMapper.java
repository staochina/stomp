package lifecycle.utils;

import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Desc:
 * Created by sun.tao on 2019/2/19
 */
@Component
public class JSONObjectRowMapper implements RowMapper<JSONObject> {
    //忽略大小写,若为 false , 则根据查询情况确定字段大小写
    private boolean ignoreUpperLowCase = false;
    //字段名称输出时，是否转成大写，默认为 false
    private boolean upperCase = false;

    /**
     * 无参构造函数
     * 将会使用默认配置
     */
    public JSONObjectRowMapper(){
        super();
    }

    /**
     * desc:
     * @param ignoreUpperLowCase 是否忽略大小写
     * @param upperCase 是否强制转换为大写
     */
    public JSONObjectRowMapper(boolean ignoreUpperLowCase, boolean upperCase){
        super();
        this.ignoreUpperLowCase =ignoreUpperLowCase;
        this.upperCase = upperCase;
    }

    @Override
    public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        //获取元数据对象
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        JSONObject json = new JSONObject();
        //根据元数据对象进行解析结果
        Object objValue;
        for(int i =1 ;i<=columnCount ;i++){
            objValue = rs.getObject(i);
            //获取元数据名称
            String name;
            if(this.ignoreUpperLowCase) {
                name=meta.getColumnName(i);
            } else {
                name=upperCase? meta.getColumnName(i).toUpperCase(): meta.getColumnName(i).toLowerCase();
            }

            if(rs.wasNull()){
                json.put(name, JSONObject.NULL);
            }
            else{
                json.put(name, this.convertClobOrDate(objValue));
            }
        }

        return json;
    }

    /**
     * Determine the key to use for the given column in the column Map.
     * @param columnName the column name as returned by the ResultSet
     * @return the column key to use
     * @see java.sql.ResultSetMetaData#getColumnName
     */
    protected String getColumnKey(String columnName) {
        return columnName;
    }

    private Object convertClobOrDate(Object obj) throws SQLException{
        if(obj instanceof java.sql.Clob) {
            obj=((java.sql.Clob)obj).getSubString(1, (int) ((java.sql.Clob)obj).length());
        }else if(obj instanceof java.util.Date){
            obj=ObjectFormat.dateTimeSecondDashFormatter(obj);
        }
        return obj;
    }

}
