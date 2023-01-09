package src.main.java.com.example.rulecheck;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RuleCheckSubMain {
    
    public static void main(String[] args) throws SecurityException, IOException {
        //Loading properties
        SubPropertiesUtil spu = SubPropertiesUtil.getInstance();

        //Preparation for log output processing
        Logger logger = Logger.getLogger(spu.SUB_LOG);
        Handler handler = new FileHandler(spu.SUB_LOG_FILE);
        logger.addHandler(handler);
        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        logger.log(Level.INFO, "Start SubMain");

        //Execute subscriber
        RuleCheckSubscriber sub = new RuleCheckSubscriber();
        sub.subscriber(logger);
    }
}
