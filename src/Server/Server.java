/*
    Name: Enda Kilgarriff
    StudentID:  17351606
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        // Create ServerSocket with fixed port
        ServerSocket serverSocket = new ServerSocket(4444);

        // Server runs forever
        while (true) {
            System.out.println("Setting up server...");

            //Create server-side client socket to accept requests
            Socket clientSocket = serverSocket.accept();

            // ClientHandler enables multithreaded server
            ClientHandler ch = new ClientHandler(clientSocket);

            //Start threads
            ch.start();
            System.out.println("Client Connected\n");
        }
    }
}

// Enable Mulithreading
class ClientHandler extends Thread {
    private Socket s; // Socket returned by ServerSocket accept() -- already connected to a client

    ClientHandler(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {

        try {
            // Create output and input stream objects for reading and writing data to/from the server
            InputStream in = s.getInputStream();
            OutputStream os = s.getOutputStream();

            DataInputStream clientData = new DataInputStream(in);
            DataOutputStream dos = new DataOutputStream(os);

            //Is the client requesting upload/download? Get fileName Client wants to upload/receive
            String upDown = clientData.readUTF();
            String fileName = clientData.readUTF();
            System.out.println("Upload/Download: " + upDown + "\n" + "Name of file: " + fileName);

            switch (upDown) {
                case ("-u"): // Client requests upload
                    System.out.println("Server receiving file from client - File:" + fileName);

                    // Reads bytes in chunks from client
                    long size = clientData.readLong();
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    OutputStream output = new FileOutputStream(fileName);

                    // While there is data available and there is data on temp buffer write to the server
                    while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                        output.write(buffer, 0, bytesRead);
                        size -= bytesRead;
                    }

                    // Closing the FileOutputStream handle
                    in.close();
                    clientData.close();
                    output.close();
                    System.out.println("File uploaded from client\n");
                    break;


                case ("-d"): //Client requests download

                    System.out.println("Server sending file to client - File: " + fileName);

                    // Get file that client wants to download
                    File file = new File(fileName);
                    byte[] byteArray = new byte[(int) file.length()];

                    //Obtains input bytes from the file and BufferedInputSteam adds the ability to buffer the input
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    //Reads the bytes from the input stream depending on the length of the file to send
                    DataInputStream dis = new DataInputStream(bis);
                    dis.readFully(byteArray, 0, byteArray.length);

                    //Sending file name and file size to the server
                    dos.writeLong(byteArray.length);
                    dos.write(byteArray, 0, byteArray.length);
                    dos.flush();

                    //Sending file data to the server
                    os.write(byteArray, 0, byteArray.length);
                    os.flush();

                    //Closing resources
                    os.close();
                    dos.close();
                    System.out.println("File sent to client\n");
                    break;
            }

            // close socket
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}