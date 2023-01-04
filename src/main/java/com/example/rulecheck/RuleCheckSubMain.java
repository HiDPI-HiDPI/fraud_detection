package src.main.java.com.example.rulecheck;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RuleCheckSubMain {
    private static LocalDateTime date = LocalDateTime.now();
    private static DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String bootTime = dataFormat.format(date);

    private static final String SUB_LOG = "sub_log";
    private static final String LOG_FILE_PATH = "./log/sub/";
    private static final String LOG_FILE_NAME = "sublog";
    private static final String SUB_LOG_FILE = LOG_FILE_PATH + LOG_FILE_NAME + "_" +bootTime + ".log";
    
    private static final int PORT = 7777;

    public static final Logger logger = Logger.getLogger(SUB_LOG);

    private static final int BUFFER_SIZE = 90 + 1;
    private static final int[] SPLIT_BUFFER_SIZE_ARRAY = {3,3,3};
    
    public static void main(String[] args) throws SecurityException, IOException {
        Logger logger = Logger.getLogger(SUB_LOG);
        Handler handler = new FileHandler(SUB_LOG_FILE);
        logger.addHandler(handler);
        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        logger.log(Level.INFO, "Start SubMain");
        subscriber();
    }

    public static void subscriber() {
        //Simplify close processing with try-with-resources
        try (
            //Open ServerSocketChannel and Selector
            ServerSocketChannel socket = ServerSocketChannel.open();
            Selector selector = Selector.open();){
            logger.log(Level.INFO, "Opne ServerSocketChannel");
            logger.log(Level.INFO, "Opne Selector");

            socket.socket().bind(new InetSocketAddress(PORT));
            socket.configureBlocking(false);            
            socket.register(selector, SelectionKey.OP_ACCEPT);

            while(selector.select() > 0){
                
                Iterator iterator = selector.selectedKeys().iterator();
                //System.out.println("while1");

                while(iterator.hasNext()){
                    SelectionKey selectionKey = (SelectionKey)iterator.next();
                    iterator.remove();
                    ByteBuffer bb = ByteBuffer.allocate(BUFFER_SIZE);

                    if (selectionKey.isAcceptable()) {
                        //System.out.println("isAcceptable");
                        ServerSocketChannel ssc = (ServerSocketChannel)selectionKey.channel();
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        
                    } else if (selectionKey.isReadable()) {
                        SocketChannel sc = (SocketChannel)selectionKey.channel();
                        //System.out.println("isReadable");
                        selectionKey = sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(BUFFER_SIZE));
                        bb =  ByteBuffer.class.cast( selectionKey.attachment() );
                        System.out.println(bb);    
                        if (sc.read(bb) > 0) {
                            bb.flip();
                            for (int i : SPLIT_BUFFER_SIZE_ARRAY) {
                                byte bytes[] = new byte[i];
                                bb.get(bytes);
                                System.out.println(new String(bytes, StandardCharsets.UTF_8));    
                            }
                            bb.position(9);
                            byte bytes[] = new byte[1];
                            bytes[0] = 'L';
                            bb.limit(10);
                            System.out.println(bb);
                            bb.put(bytes);
                            bb.flip();
                            sc.write(bb);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }
}
