package lifecycle.Interceptor;

import lifecycle.auth.PortalPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * 检查握手请求和响应, 对WebSocketHandler传递属性
 */
public class STOMPHandshakeHandler extends DefaultHandshakeHandler {
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String uidStr= ((ServletServerHttpRequest) request).getServletRequest().getHeader("uid");
        String name= ((ServletServerHttpRequest) request).getServletRequest().getHeader("name");
        logger.debug("------  uidStr:"+uidStr);
        logger.debug("------  name:"+name);
        int uid = Integer.parseInt(uidStr);
        PortalPrincipal principal = new PortalPrincipal(uid,name);
        return principal;
    }

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

    /*public boolean beforeHandshake(ServletServerHttpRequest request, ServletServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String uidStr= ((ServletServerHttpRequest) request).getServletRequest().getParameter("uid");
        String name= ((ServletServerHttpRequest) request).getServletRequest().getParameter("name");
        logger.debug("======================Interceptor uid:" + uidStr);
        logger.debug("======================Interceptor name :" + name);
        //保存客户端标识
        int uid = Integer.parseInt(uidStr);
        if(3 == uid ){
            return  false;
        }
        PortalPrincipal principal = new PortalPrincipal(uid,name);
        attributes.put("principal",principal);
        return true;
    }*/

}
