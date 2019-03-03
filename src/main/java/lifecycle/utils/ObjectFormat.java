package lifecycle.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Desc:
 * Created by sun.tao on 2019/2/19
 */
public class ObjectFormat {
    private static SimpleDateFormat dateTimeSecondDashFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String dateTimeSecondDashFormatter(Object dateObject){
        try {
            Date date =(Date)dateObject;
            return dateTimeSecondDashFormatter.format(date);
        }
        catch (Exception exp){
            return null;
        }

    }
}
