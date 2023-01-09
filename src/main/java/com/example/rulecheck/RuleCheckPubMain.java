package src.main.java.com.example.rulecheck;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RuleCheckPubMain {
    
    public static void main(String[] args) throws SecurityException, IOException {

        //Loading properties
        PubPropertiesUtil ppu = PubPropertiesUtil.getInstance();

        //Preparation for log output processing
        Logger logger = Logger.getLogger(ppu.PUB_LOG);
        Handler handler = new FileHandler(ppu.PUB_LOG_FILE);
        logger.addHandler(handler);
        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        logger.log(Level.INFO, "Start PubMain");

        //String to publish
        String pubData = args[0];
        logger.log(Level.INFO, "Pub:" + pubData);
        
        //Execute publisher
        RuleCheckPublisher ruleCheckPublisher = new RuleCheckPublisher();
        ruleCheckPublisher.pubscriber(pubData, logger);
    }   
}
