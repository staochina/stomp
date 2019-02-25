package com.utils;

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
    //���Դ�Сд,��Ϊ false , ����ݲ�ѯ���ȷ���ֶδ�Сд
    private boolean ignoreUpperLowCase = false;
    //�ֶ��������ʱ���Ƿ�ת�ɴ�д��Ĭ��Ϊ false
    private boolean upperCase = false;

    /**
     * �޲ι��캯��
     * ����ʹ��Ĭ������
     */
    public JSONObjectRowMapper(){
        super();
    }

    /**
     * desc:
     * @param ignoreUpperLowCase �Ƿ���Դ�Сд
     * @param upperCase �Ƿ�ǿ��ת��Ϊ��д
     */
    public JSONObjectRowMapper(boolean ignoreUpperLowCase, boolean upperCase){
        super();
        this.ignoreUpperLowCase =ignoreUpperLowCase;
        this.upperCase = upperCase;
    }

    @Override
    public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        //��ȡԪ���ݶ���
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        JSONObject json = new JSONObject();
        //����Ԫ���ݶ�����н������
        Object objValue;
        for(int i =1 ;i<=columnCount ;i++){
            objValue = rs.getObject(i);
            //��ȡԪ��������
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
