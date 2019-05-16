package lifecycle.utils;

/**
 * Desc:
 * Created by sun.tao on 2019/5/6
 */

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 使用 HttpClient 连接池
 *
 * @author luchao
 *
 */
@Component
public class HttpClientSingleton {
    private static Logger logger = LoggerFactory.getLogger(HttpClientSingleton.class);
    static PoolingHttpClientConnectionManager manager = null;
    static CloseableHttpClient httpClient = null;

    private static CloseableHttpClient getHttpClient() {
        // 注册访问协议相关的Socket工厂
        Registry<ConnectionSocketFactory> socketFactoryRegistory = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSystemSocketFactory()).build();
        // HttpConnection工厂:配置写请求/解析响应处理器
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                DefaultHttpRequestWriterFactory.INSTANCE, DefaultHttpResponseParserFactory.INSTANCE);

        // DNS 解析
        DnsResolver dnsResolver =new SystemDefaultDnsResolver();
        logger.debug("333333");
        // 创建池连接化连接管理器
        manager = new PoolingHttpClientConnectionManager(socketFactoryRegistory, connFactory, dnsResolver);

        // 默认为socker 配置
        SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        manager.setDefaultSocketConfig(defaultSocketConfig);
        manager.setMaxTotal(300);// 设置整个连接池的最大连接数
        // 每个路由的默认最大连接，每个路由实际最大连接数
        // DefaultMaxPerRoute 控制，而MaxTotal是控制整个池子最大数
        manager.setDefaultMaxPerRoute(200);// 每个人路由最大连接数
        manager.setValidateAfterInactivity(8 * 1000);// 在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s

        // 默认请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(180 * 1000)// 设置连接超时时间2s
                .setSocketTimeout(180 * 1000)// 设置等待数据超时时间，5s
                .setConnectionRequestTimeout(20 * 1000)// 设置从连接池获取连接的等待超时时间
                .build();

        // 创建 HttpClient
        HttpClientBuilder httpClientBuiler = HttpClients.custom().setConnectionManager(manager).setConnectionManagerShared(false) // 连接池是不是共享模式
                .evictIdleConnections(60, TimeUnit.SECONDS)// 定时回收空闲连接
                .evictExpiredConnections()// 定时回收过期连接
                .setConnectionTimeToLive(60, TimeUnit.SECONDS)// 连接存活时间，如果不设置，则根据长连接
                .setDefaultRequestConfig(defaultRequestConfig)// 设置默认请求配置
                .setConnectionReuseStrategy(new DefaultConnectionReuseStrategy())// 连接重用策略，即获取长连接生产多长时间
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());// 长连接配置，即获取长连接生产多长时间
        //.setRetryHandler(new DefaultHttpRequestRetryHandler(0, ));// 设置重试次数，默认是3次；当前是禁用掉

        //final String proxyIpAddress = ConfigValues.get("proxy.ip.address", "");
        //设置代理
        /*if(!proxyIpAddress.isEmpty()){
            HttpRoutePlanner routePlanner = new HttpRoutePlanner() {
                @Override
                public HttpRoute determineRoute( HttpHost target, HttpRequest request,
                                                 HttpContext context) throws HttpException {
                    return new HttpRoute(target, null, new HttpHost(proxyIpAddress,Integer.parseInt(ConfigValues.get("proxy.port", ""))),
                            "https".equalsIgnoreCase(target.getSchemeName()));
                }
            };
            httpClientBuiler.setRoutePlanner(routePlanner);
        }*/

        httpClient = httpClientBuiler.build();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return httpClient;
    }

    public static final CloseableHttpClient getInstance() {
        return HttpClientSingletonHolder.INSTANCE;
    }

    // 静态内部类实现单列
    private static class HttpClientSingletonHolder {
        private static final CloseableHttpClient INSTANCE = HttpClientSingleton.getHttpClient();

    }
}
