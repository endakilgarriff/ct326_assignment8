package assignment8;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws Exception{
        Client client = new Client();
        client.run();
    }

    public void run() throws Exception {
        Socket soc = new Socket("localhost", 666);
        PrintStream ps = new PrintStream(soc.getOutputStream());
        ps.println("Hello to server from client");

        InputStreamReader ir = new InputStreamReader(soc.getInputStream());
        BufferedReader br = new BufferedReader(ir);

        String message = br.readLine();
        System.out.println(message);
    }
}
