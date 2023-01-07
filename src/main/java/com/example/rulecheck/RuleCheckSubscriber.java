package src.main.java.com.example.rulecheck;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RuleCheckSubscriber {
    private static final int[] SPLIT_BUFFER_SIZE_ARRAY = {3,3,3};
    PropertiesUtil pu = PropertiesUtil.getInstance();

    
    public void subscriber(Logger logger, PropertiesUtil pu) {
        //Simplify close processing with try-with-resources
        try (
            //Open ServerSocketChannel and Selector
            ServerSocketChannel socket = ServerSocketChannel.open();
            Selector selector = Selector.open();){


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
                        SubAccess(selector, selectionKey);
                    } else if (selectionKey.isReadable()) {
                        SubRead(selector, selectionKey, bb);
                    }
                }
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }

    void SubRead(Selector selector, SelectionKey selectionKey, ByteBuffer bb) throws IOException {
        SocketChannel sc = (SocketChannel)selectionKey.channel();
        selectionKey = sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(PropertiesUtil.BUFFER_SIZE));
        bb =  ByteBuffer.class.cast( selectionKey.attachment() );  
        if (sc.read(bb) > 0) {
            bb.flip();
            for (int i : SPLIT_BUFFER_SIZE_ARRAY) {
                byte bytes[] = new byte[i];
                bb.get(bytes);
            }
            bb.position(9);
            byte bytes[] = new byte[1];
            bytes[0] = 'L';
            bb.limit(10);

            bb.put(bytes);
            bb.flip();
            sc.write(bb);
        }
    }

    void SubAccess(Selector selector, SelectionKey selectionKey) throws IOException{
        ServerSocketChannel ssc = (ServerSocketChannel)selectionKey.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
    }

    
}
