package lifecycle.model;

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
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
