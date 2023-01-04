package src.main.java.com.example.rulecheck;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class PropertiesUtil {
    private static LocalDateTime date = LocalDateTime.now();
    private static DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String bootTime = dataFormat.format(date);

    private static final String PROPATIES_PATH = "./properties/sub.properties";
    
    public String SUB_LOG;
    public String LOG_FILE_PATH;
    public String LOG_FILE_NAME;
    public String SUB_LOG_FILE;
    public int PORT;
    public int BUFFER_SIZE;

    public void Constructor() throws IOException{

        Properties properties = new Properties();
        FileInputStream in = null;
        in = new FileInputStream(PROPATIES_PATH);
        properties.load(in);
        this.SUB_LOG = properties.getProperty("src.main.java.com.example.rulecheck.SUB_LOG");
        this.LOG_FILE_PATH = properties.getProperty("src.main.java.com.example.rulecheck.LOG_FILE_PATH");
        this.LOG_FILE_NAME = properties.getProperty("src.main.java.com.example.rulecheck.LOG_FILE_NAME");
        this.SUB_LOG_FILE = LOG_FILE_PATH + LOG_FILE_NAME + "_" + bootTime + ".log";
        this.PORT = Integer.parseInt( properties.getProperty("src.main.java.com.example.rulecheck.PORT") );
        this.BUFFER_SIZE = Integer.parseInt(
            properties.getProperty("src.main.java.com.example.rulecheck.BUFFER_SIZE") ) + 1;
    }

}
