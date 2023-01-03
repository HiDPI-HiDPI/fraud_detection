package src.main.java.com.example.rulecheck;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RuleCheckPubMain {
    private static LocalDateTime date = LocalDateTime.now();
    private static DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String bootTime = dataFormat.format(date);

    private static final String PUB_LOG = "pub_log";
    private static final String LOG_FILE_PATH = "./log/pub/";
    private static final String LOG_FILE_NAME = "publog";
    private static final String PUB_LOG_FILE = LOG_FILE_PATH + LOG_FILE_NAME + "_" +bootTime + ".log";

    private static final String IP_ADDRESS = "192.168.0.64";
    private static final int PORT = 7777;

    public static final Logger logger = Logger.getLogger(PUB_LOG);

    public static void main(String[] args) throws SecurityException, IOException {
        Logger logger = Logger.getLogger(PUB_LOG);
        Handler handler = new FileHandler(PUB_LOG_FILE);
        logger.addHandler(handler);
        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        logger.log(Level.INFO, "Start PubMain");
        //String to publish
        String pubData = args[0];
        logger.log(Level.INFO, "Pub:" + pubData);
        
        pubscriber(pubData);
    }

    public static void pubscriber(String pubData) {
        //Simplify close processing with try-with-resources
        try (
            //Open SocketChannel 
            SocketChannel socket = SocketChannel.open(new InetSocketAddress(IP_ADDRESS, PORT));){
            logger.log(Level.INFO, "Opne SocketChannel");

            


            
            //Put string in ByteBuffer
            ByteBuffer bb = ByteBuffer.allocate(1024);
            ByteBuffer bb_r = bb.put(pubData.getBytes(StandardCharsets.UTF_8));

            //Pub！
            bb_r.flip();
            socket.write(bb_r);
            
            //Return from sub
            bb_r.clear();
            socket.read(bb_r);
            bb.flip();
            logger.log(Level.INFO, "Return from sub:" + StandardCharsets.UTF_8.decode(bb).toString());
            logger.log(Level.INFO, "End PubMain!");
             
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        

    }
}
