package src.main.java.com.example.rulecheck;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class RuleCheckSubMain {
    public static void main(String[] args) {
        System.out.println("Hello World !");
        subscriber();
    }

    public static void subscriber() {
        try (
            ServerSocketChannel socket = ServerSocketChannel.open();
            Selector selector = Selector.open();){


            socket.socket().bind(new InetSocketAddress(7777));
            SocketChannel sc = socket.accept();
            sc.configureBlocking(false);
            
            SelectionKey key = sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
            selector.select();
            ByteBuffer bb =  ByteBuffer.class.cast( key.attachment() );
            sc.read(bb);
            bb.flip();

            System.out.println(StandardCharsets.UTF_8.decode(bb).toString());


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }


    }
}
