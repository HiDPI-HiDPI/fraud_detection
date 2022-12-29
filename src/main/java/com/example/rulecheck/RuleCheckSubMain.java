package src.main.java.com.example.rulecheck;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RuleCheckSubMain {
    public static void main(String[] args) {
        System.out.println("Hello World !");
        subscriber();
    }

    public static void subscriber() {
        try {
            ServerSocket socket = new ServerSocket(7777);
            Socket subSocket = socket.accept();
            byte[] data = new byte[1024];
            InputStream in = subSocket.getInputStream();
            int readSize = in.read(data);
            data = Arrays.copyOf(data, readSize);
            String s = new String(data, StandardCharsets.UTF_8);
            System.out.println(s);
            in.close();
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        

    }
}
