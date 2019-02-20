package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Desc:
 * Created by sun.tao on 2019/2/20
 */
public class Greeting {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        logger.debug("初始化 Greeting");
        this.content = content;
    }

    public String getContent() {
        logger.debug("初始化 content");
        return content;
    }
}
