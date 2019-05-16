package lifecycle.RowMapperImpl;

import lifecycle.model.FairMessage;
import lifecycle.utils.ObjectFormat;
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
public class FairMessageRowMapper implements RowMapper<FairMessage> {

    public FairMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
        FairMessage fairMessage = new FairMessage();
        fairMessage.setId(rs.getInt("id"));
        fairMessage.setFromId(rs.getInt("from_id"));
        fairMessage.setToId(rs.getInt("to_id"));
        fairMessage.setType(rs.getString("type"));
        fairMessage.setStatus(rs.getString("status"));
        fairMessage.setDealtime(rs.getString("dealtime"));
        fairMessage.setContent(rs.getString("content"));
        return fairMessage;
    }
}
