package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Desc:
 * Created by sun.tao on 2019/2/20
 */
public class HelloMessage {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private int uid;
    private String name;

    public HelloMessage() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public HelloMessage(int uid, String name) {
        logger.debug("初始化 HelloMessage class");
        this.uid = uid;
        this.name = name;
    }

    public String getName() {
        logger.debug("获取 name");
        return name;
    }

    public void setName(String name) {
        logger.debug("组装 name");
        this.name = name;
    }
}
