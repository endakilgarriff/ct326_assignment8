package assignment8;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws Exception{
        // write your code here
        Server server = new Server();
        Server.run();
        System.out.println("Server started");
    }

    public static void run() throws Exception {
        ServerSocket sersoc = new ServerSocket(666);
        Socket soc = sersoc.accept();
        InputStreamReader ir = new InputStreamReader(soc.getInputStream());
        BufferedReader br = new BufferedReader(ir);

        String message = br.readLine();
//        System.out.println(message);

        if(message != null){
            PrintStream ps = new PrintStream(soc.getOutputStream());
            ps.println("Message received: " + message);
        }

    }
}
