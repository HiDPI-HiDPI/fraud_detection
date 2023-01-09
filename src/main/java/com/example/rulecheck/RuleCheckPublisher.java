package src.main.java.com.example.rulecheck;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RuleCheckPublisher {
    PubPropertiesUtil ppu = PubPropertiesUtil.getInstance();

    public void pubscriber(String pubData, Logger logger) {
        
        //Simplify close processing with try-with-resources
        try (
            //Open SocketChannel 
            SocketChannel socket = SocketChannel.open(new InetSocketAddress(ppu.IP_ADDRESS, ppu.PORT));){
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
