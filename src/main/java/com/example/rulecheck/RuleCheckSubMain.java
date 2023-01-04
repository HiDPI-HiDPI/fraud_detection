package src.main.java.com.example.rulecheck;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RuleCheckSubMain {
    
    private static final int[] SPLIT_BUFFER_SIZE_ARRAY = {3,3,3};
    static PropertiesUtil pu = new PropertiesUtil();
    
    public static void main(String[] args) throws SecurityException, IOException {
        pu.Constructor();
        Logger logger = Logger.getLogger(pu.SUB_LOG);
        Handler handler = new FileHandler(pu.SUB_LOG_FILE);
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
            PropertiesUtil pu = new PropertiesUtil();
            pu.Constructor();
            Logger logger = Logger.getLogger(pu.SUB_LOG);
            logger.log(Level.INFO, "Opne ServerSocketChannel");
            logger.log(Level.INFO, "Opne Selector");

            socket.socket().bind(new InetSocketAddress(pu.PORT));
            socket.configureBlocking(false);            
            socket.register(selector, SelectionKey.OP_ACCEPT);

            while(selector.select() > 0){
                
                Iterator iterator = selector.selectedKeys().iterator();

                while(iterator.hasNext()){
                    SelectionKey selectionKey = (SelectionKey)iterator.next();
                    iterator.remove();
                    ByteBuffer bb = ByteBuffer.allocate(pu.BUFFER_SIZE);

                    if (selectionKey.isAcceptable()) {
                        //System.out.println("isAcceptable");
                        ServerSocketChannel ssc = (ServerSocketChannel)selectionKey.channel();
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        
                    } else if (selectionKey.isReadable()) {
                        SocketChannel sc = (SocketChannel)selectionKey.channel();
                        //System.out.println("isReadable");
                        selectionKey = sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(pu.BUFFER_SIZE));
                        bb =  ByteBuffer.class.cast( selectionKey.attachment() );
                        //System.out.println(bb);    
                        if (sc.read(bb) > 0) {
                            bb.flip();
                            for (int i : SPLIT_BUFFER_SIZE_ARRAY) {
                                byte bytes[] = new byte[i];
                                bb.get(bytes);
                                //System.out.println(new String(bytes, StandardCharsets.UTF_8));    
                            }
                            bb.position(9);
                            byte bytes[] = new byte[1];
                            bytes[0] = 'L';
                            bb.limit(10);
                            //System.out.println(bb);
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
