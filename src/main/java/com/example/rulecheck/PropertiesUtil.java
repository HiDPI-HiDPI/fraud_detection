package src.main.java.com.example.rulecheck;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class PropertiesUtil {
    private static PropertiesUtil propertiesUtil = new PropertiesUtil();


    private static LocalDateTime date = LocalDateTime.now();
    private static DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String bootTime = dataFormat.format(date);

    private static final String PROPATIES_PATH = "./properties/sub.properties";
    
    public static String SUB_LOG;
    public static String LOG_FILE_PATH;
    public static String LOG_FILE_NAME;
    public static String SUB_LOG_FILE;
    public static int PORT;
    public static int BUFFER_SIZE;

    private PropertiesUtil() {

        System.out.println("インスタンスが生成されました");

        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(PROPATIES_PATH);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            properties.load(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SUB_LOG = properties.getProperty("src.main.java.com.example.rulecheck.SUB_LOG");
        LOG_FILE_PATH = properties.getProperty("src.main.java.com.example.rulecheck.LOG_FILE_PATH");
        LOG_FILE_NAME = properties.getProperty("src.main.java.com.example.rulecheck.LOG_FILE_NAME");
        SUB_LOG_FILE = LOG_FILE_PATH + LOG_FILE_NAME + "_" + bootTime + ".log";
        PORT = Integer.parseInt( properties.getProperty("src.main.java.com.example.rulecheck.PORT") );
        BUFFER_SIZE = Integer.parseInt(
            properties.getProperty("src.main.java.com.example.rulecheck.BUFFER_SIZE") ) + 1;
    }

    public static PropertiesUtil getInstance(){
        return propertiesUtil;
    }

    public String getValue(){
        return SUB_LOG;
    }



}
