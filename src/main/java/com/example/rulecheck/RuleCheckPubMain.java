package src.main.java.com.example.rulecheck;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class RuleCheckPubMain {
    public static void main(String[] args) {
        System.out.println("Hello World !");
        pubscriber();
    }

    public static void pubscriber() {
        try (
            SocketChannel socket = SocketChannel.open(new InetSocketAddress("192.168.0.64", 7777));){
            System.out.println("Start!");

            String pubData = "TEST";
            ByteBuffer bb = ByteBuffer.allocate(1024);
            ByteBuffer bb_r = bb.put(pubData.getBytes(StandardCharsets.UTF_8));
            bb_r.flip();
            System.out.println(bb_r);
            socket.write(bb_r);

            System.out.println("End!");
             
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        

    }
}
