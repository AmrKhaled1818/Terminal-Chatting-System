import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPServer {

    public static void main(String[] args) {
        // Check if port and server name are provided
        if (args.length < 2) {
            System.out.println("Usage: java TCPServer <port> <serverName>");
            return;
        }

        int port = Integer.parseInt(args[0]); // Get port number from command line
        String serverName = args[1]; // Get server's name from command line

        try {
            // Create a server socket bound to the specified port
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

            // Accept a client connection
            Socket server = serverSocket.accept();
            System.out.println("Just connected to " + server.getRemoteSocketAddress());

            // Get output stream to send data to the client
            OutputStream outToClient = server.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToClient);

            // Get input stream to receive data from the client
            InputStream inFromClient = server.getInputStream();
            DataInputStream in = new DataInputStream(inFromClient);

            Scanner scanner = new Scanner(System.in);

            // Read CONNECT message from client
            String connectMessage = in.readUTF();
            if (!"CONNECT".equalsIgnoreCase(connectMessage)) {
                System.out.println("Invalid connect message. Closing connection.");
                server.close();
                return;
            }

            // Send confirmation to client
            out.writeUTF("CONNECTED");

            // Read client name from client
            String clientName = in.readUTF();
            // Send server name to client
            out.writeUTF(serverName);

            // Create a thread to read messages from the client
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        // Read message from client
                        String response = in.readUTF();
                        System.out.println(clientName + " says: " + response);
                        if ("CLOSE".equalsIgnoreCase(response)) {
                            System.out.println("Client requested to close the connection.");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed by client.");
                }
            });

            // Start the thread
            readThread.start();

            // Main thread to send messages to the client
            while (true) {
                System.out.print("\n" + serverName + " is typing: "); // Display server's name
                String message = scanner.nextLine();
                // Send message to client in uppercase
                out.writeUTF(message.toUpperCase());
                if (message.equalsIgnoreCase("CLOSE")) {
                    break;
                }
            }

            // Close the connection and scanner
            server.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
