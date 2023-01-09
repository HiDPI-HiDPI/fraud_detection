package src.main.java.com.example.rulecheck;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class PubPropertiesUtil {
    private static PubPropertiesUtil pubPropertiesUtil = new PubPropertiesUtil();

    //Get instantiation time
    private final LocalDateTime date = LocalDateTime.now();
    private final DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private final String bootTime = dataFormat.format(date);

    //Define properties file path
    private final String PROPATIES_PATH = "./properties/pub.properties";

    //Values in the properties file
    public final String PUB_LOG;
    public final String LOG_FILE_PATH;
    public final String LOG_FILE_NAME;
    public final String PUB_LOG_FILE;
    public final String IP_ADDRESS;
    public final int PORT;
    public final int BUFFER_SIZE;

    private PubPropertiesUtil() {

        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(PROPATIES_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PUB_LOG       = properties.getProperty("src.main.java.com.example.rulecheck.PUB_LOG");
        LOG_FILE_PATH = properties.getProperty("src.main.java.com.example.rulecheck.LOG_FILE_PATH");
        LOG_FILE_NAME = properties.getProperty("src.main.java.com.example.rulecheck.LOG_FILE_NAME");
        PUB_LOG_FILE  = LOG_FILE_PATH + LOG_FILE_NAME + "_" + bootTime + ".log";
        IP_ADDRESS    = properties.getProperty("src.main.java.com.example.rulecheck.IP_ADDRESS");
        PORT          = Integer.parseInt( properties.getProperty("src.main.java.com.example.rulecheck.PORT") );
        BUFFER_SIZE   = Integer.parseInt(
            properties.getProperty("src.main.java.com.example.rulecheck.BUFFER_SIZE") ) + 1;
    }

    public static PubPropertiesUtil getInstance(){
        return pubPropertiesUtil;
    }
}
