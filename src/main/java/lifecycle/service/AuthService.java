package lifecycle.service;

import lifecycle.dao.impl.MessageDao;
import lifecycle.utils.HttpClientSingleton;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Desc:
 * Created by sun.tao on 2019/5/6
 */
@Service
public class AuthService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private HttpClientSingleton httpClientSingleton;
    @Value("${services.auth.url}")
    private String authUrl;

    @Autowired
    MessageDao messageDao;

    /**
     * 获取用户信息
     * @param params {cmd:"auth" , userKey:"用户的sessionkey", uid:"用户的id"  }
     * @return
     */
    public JSONObject getUserInfo(JSONObject params){
         JSONArray list  = new JSONArray();
         JSONObject fair = new JSONObject();
         fair.put("id",0);
         fair.put("command","com.agilecontrol.fair.FairCmd");
         fair.put("params",params);
         list.put(fair);

        CloseableHttpClient httpClient = getHttpClient();
        JSONObject resultAsJson = null;
        try {
            HttpPost post = new HttpPost(authUrl);
            StringEntity stringEntity = new StringEntity(list.toString(), ContentType.create("application/json", "UTF-8"));
            post.setEntity(stringEntity);
            logger.debug("-----111");
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            logger.debug("-----222");
            try{
                HttpEntity entity = httpResponse.getEntity();
                logger.debug("-----333");
                if (null != entity){
                    String result = EntityUtils.toString(entity, Consts.UTF_8);
                    logger.debug("---- result : "+result);
                    resultAsJson = new JSONArray(result).getJSONObject(0);
                    logger.debug("-------------------------------------------------------");
                    logger.debug(EntityUtils.toString(entity));
                    logger.debug("-------------------------------------------------------");
                }
            } finally{
                httpResponse.close();
            }

        } catch( UnsupportedEncodingException e){
            logger.debug(e.toString());
        }
        catch (IOException e) {
            logger.debug(e.toString());
        }
        finally{
            try{
                closeHttpClient(httpClient);
            } catch(Exception e){
                logger.debug(e.toString());
            }
        }
        return resultAsJson;
    }

    private CloseableHttpClient getHttpClient(){
        return HttpClients.createDefault();
    }

    private void closeHttpClient(CloseableHttpClient client) throws IOException{
        if (client != null){
            client.close();
        }
    }
}
