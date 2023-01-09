package src.main.java.com.example.rulecheck;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.stream.Stream;

public class SubPropertiesUtil {
    private static SubPropertiesUtil subPropertiesUtil = new SubPropertiesUtil();

    //Get instantiation time
    private final LocalDateTime date = LocalDateTime.now();
    private final DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private final String bootTime = dataFormat.format(date);

    //Define properties file path
    private final String PROPATIES_PATH = "./properties/sub.properties";

    //Values in the properties file
    public final String SUB_LOG;
    public final String LOG_FILE_PATH;
    public final String LOG_FILE_NAME;
    public final String SUB_LOG_FILE;
    public final int PORT;
    public final int BUFFER_SIZE;
    public final int[] SPLIT_BUFFER_SIZE_ARRAY;
    public final String RESULT;

    private SubPropertiesUtil() {

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
        SUB_LOG                 = properties.getProperty("src.main.java.com.example.rulecheck.SUB_LOG");
        LOG_FILE_PATH           = properties.getProperty("src.main.java.com.example.rulecheck.LOG_FILE_PATH");
        LOG_FILE_NAME           = properties.getProperty("src.main.java.com.example.rulecheck.LOG_FILE_NAME");
        SUB_LOG_FILE            = LOG_FILE_PATH + LOG_FILE_NAME + "_" + bootTime + ".log";
        PORT                    = Integer.parseInt( properties.getProperty("src.main.java.com.example.rulecheck.PORT") );
        BUFFER_SIZE             = Integer.parseInt(
            properties.getProperty("src.main.java.com.example.rulecheck.BUFFER_SIZE") ) + 1;
        SPLIT_BUFFER_SIZE_ARRAY = Stream.of(
            properties.getProperty("src.main.java.com.example.rulecheck.SPLIT_BUFFER_SIZE_ARRAY").split(",")
            ).mapToInt(Integer::parseInt).toArray();
        RESULT                  = properties.getProperty("src.main.java.com.example.rulecheck.RESULT");
    }

    public static SubPropertiesUtil getInstance(){
        return subPropertiesUtil;
    }
}
