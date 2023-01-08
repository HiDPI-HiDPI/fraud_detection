package src.main.java.com.example.rulecheck;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RuleCheckSubscriber {
    private static final int[] SPLIT_BUFFER_SIZE_ARRAY = {3,3,3};
    PropertiesUtil pu = PropertiesUtil.getInstance();

    public void subscriber(Logger logger) {
        //Simplify close processing with try-with-resources
        try (
            //Open ServerSocketChannel and Selector
            ServerSocketChannel socket = ServerSocketChannel.open();
            Selector selector = Selector.open();){
            logger.log(Level.INFO, "Opne ServerSocketChannel");
            logger.log(Level.INFO, "Opne Selector");

            //Bind a socket on the configured port
            socket.socket().bind(new InetSocketAddress(pu.PORT));
            logger.log(Level.INFO, "Bind a socket on " + pu.PORT);

            //Blocking mode is false
            socket.configureBlocking(false);
            logger.log(Level.INFO, "Blocking mode is false");

            //Register socket and Key (OP_ACCEPT) in selector
            socket.register(selector, SelectionKey.OP_ACCEPT);
            logger.log(Level.INFO, "Register socket and Key (OP_ACCEPT) in selector");

            //Monitor with selector
            while (selector.select() > 0) {

                //Generate Iterator from Key
                Iterator iterator = selector.selectedKeys().iterator();
                
                //Start processing for each Key
                while(iterator.hasNext()){

                    //Get Key from Iterator
                    SelectionKey selectionKey = (SelectionKey)iterator.next();
                    
                    //Remove Key from Iterator
                    iterator.remove();

                    //Determine Key state
                    if (selectionKey.isAcceptable()) {
                        SubAccess(selector, selectionKey);
                    } else if (selectionKey.isReadable()) {
                        SubIO(selector, selectionKey);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void SubAccess(Selector selector, SelectionKey selectionKey) throws IOException{

        //Get channel for selectionKey
        ServerSocketChannel ssc = (ServerSocketChannel)selectionKey.channel();

        //Enable data communication with accept()
        SocketChannel sc = ssc.accept();

        //Blocking mode is false
        sc.configureBlocking(false);

        //Register socket and Key (OP_READ) in selector and set ByteBuffer. 
        sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(pu.BUFFER_SIZE));
    }

    void SubIO(Selector selector, SelectionKey selectionKey) throws IOException {

        //Get channel for selectionKey
        SocketChannel sc = (SocketChannel)selectionKey.channel();

        //Prepare ByteBuffer
        ByteBuffer bb = ByteBuffer.allocate(pu.BUFFER_SIZE);
        bb =  ByteBuffer.class.cast( selectionKey.attachment() );

        //Check read() status
        //Start sending and receiving when "sc.read(bb) > 0"
        if (sc.read(bb) > 0) {

            //Move to byte 0 and set limit to current
            bb.flip();

            //CharsetDecoder
            Charset UTF8 = StandardCharsets.UTF_8;
            CharsetDecoder cd = UTF8.newDecoder().reset();
            CharBuffer out = CharBuffer.allocate(pu.BUFFER_SIZE);
            cd.decode(bb, out, false);
            out.flip();

            //Todo : Do something nice here
            for (int i : SPLIT_BUFFER_SIZE_ARRAY) {
                char chars[] = new char[i];
                out.get(chars);
                String s = new String(chars);
                //System.out.println(s);
            }

            //Move to the end of the receive buffer
            int LAST_POS = Arrays.stream(SPLIT_BUFFER_SIZE_ARRAY).sum();
            bb.position(LAST_POS);
            
            //Set the result to the end of the receive buffer
            byte bytes[] = new byte[1];
            bytes = pu.RESULT.getBytes();
            bb.limit(pu.BUFFER_SIZE);
            bb.put(bytes);

            //Return to Pub
            bb.flip();
            sc.write(bb);
            
        } else {
            sc.close();
        }
    }
}
