/*
    Name: Enda Kilgarriff
    StudentID:  17351606
 */

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {

        //Take arguments in from command line
        String ipAddress = args[0];
        int port = Integer.parseInt(args[1]);
        String upDown = args[2];
        String fileName = args[3];

        // Print out arguments for verification
        System.out.println("IPAddress: " + ipAddress + "\n" + "Port :" + port);

        //Create client socket
        Socket s = new Socket(ipAddress, port);
        System.out.println("Connected to Server");

        //Create file object for desired file
        File file = new File(fileName);

        // Create output and input stream objects for reading and writing data to/from the client
        OutputStream os = s.getOutputStream();
        InputStream in = s.getInputStream();

        // Create data input and output stream objects for sending/receiving data to/from server
        DataInputStream server = new DataInputStream(in);
        DataInputStream serverData = new DataInputStream(in);
        DataOutputStream dos = new DataOutputStream(os);

        // send if uploading/downloading and the name of the file to upload/download
        dos.writeUTF(upDown);
        dos.writeUTF(file.getName());

        switch (upDown) {

            case ("-u"):  // File Upload
                // Details of operation and variable with size of file to send
                System.out.println("Uploading file from client to server");
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

                //Closing socket and data output stream
                os.close();
                dos.close();
                System.out.println("File Uploaded to server");
                break;


            case ("-d"): // File download

                System.out.println("Downloading file from server to client");

                // Reads bytes in 8 byte chunks from client
                long size = serverData.readLong();
                byte[] buffer = new byte[1024];
                int bytesRead; // byte variable to read bytes sent from server
                OutputStream output = new FileOutputStream(fileName);
                // While there is data available and there is data on temp buffer write to the client
                while (size > 0 && (bytesRead = serverData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                    output.write(buffer, 0, bytesRead);
                    size -= bytesRead;
                }

                // Closing resources
                in.close();
                serverData.close();
                output.close();
                System.out.println("File downloaded from server");
                break;
        }
        // Close socket
        s.close();
    }

}

// Sources: https://coderanch.com/t/556838/java/Transferring-file-file-data-socket