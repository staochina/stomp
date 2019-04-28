package lifecycle.Interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Desc:
 * Created by sun.tao on 2019/2/28
 * @author sun.tao
 * @date 2019/2/28
 */
public class HandleShakeInterceptors implements HandshakeInterceptor {
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    /**
     * 在握手之前执行该方法, 继续握手返回true, 中断握手返回false.
     * 通过attributes参数设置WebSocketSession的属性
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        Map<String, String[]> params = ((ServletServerHttpRequest) request).getServletRequest().getParameterMap();

        logger.debug("===================  HandleShakeInterceptors before " );
        //保存客户端标识
        //attributes.put("name", "8888");
        return true;
    }

    /**
     * 在握手之后执行该方法. 无论是否握手成功都指明了响应状态码和相应头.
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        logger.debug("===================  HandleShakeInterceptors after");

    }
}
