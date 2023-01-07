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
        
        PropertiesUtil pu = PropertiesUtil.getInstance();
        System.out.println(pu.getValue());

        Logger logger = Logger.getLogger("sub_log");
        Handler handler = new FileHandler(pu.SUB_LOG_FILE);
        logger.addHandler(handler);
        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        logger.log(Level.INFO, "Start SubMain");

        RuleCheckSubscriber sub = new RuleCheckSubscriber();
        sub.subscriber(logger, pu);
    }


}
