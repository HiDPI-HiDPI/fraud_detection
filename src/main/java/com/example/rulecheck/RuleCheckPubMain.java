package src.main.java.com.example.rulecheck;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RuleCheckPubMain {
    public static void main(String[] args) {
        System.out.println("Hello World !");
        pubscriber();
    }

    public static void pubscriber() {
        try {
            System.out.println("開始！");

            Socket socket = new Socket("192.168.0.64", 7777);
            OutputStream out = socket.getOutputStream();
            String pubData = "てすとでーす！";
            out.write(pubData.getBytes(StandardCharsets.UTF_8));
            out.close();

            System.out.println("終了！");
            
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        

    }
}
