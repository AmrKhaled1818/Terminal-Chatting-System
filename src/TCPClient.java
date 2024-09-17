import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {

    public static void main(String[] args) {
        // Check if server name, port, and client name are provided
        if (args.length < 3) {
            System.out.println("Usage: java TCPClient <serverName> <port> <name>");
            return;
        }

        String serverName = args[0]; // Get server's name from command line
        int port = Integer.parseInt(args[1]); // Get port number from command line
        String clientName = args[2]; // Get client's name from command line

        Scanner scanner = new Scanner(System.in);

        // Wait for user to type "CONNECT"
        System.out.println("Type 'CONNECT' to start the chat:");
        String userInput = scanner.nextLine();
        while (!"CONNECT".equalsIgnoreCase(userInput)) {
            System.out.println("Invalid input. Please type 'CONNECT' to start the chat:");
            userInput = scanner.nextLine();
        }

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            // Create a client socket and connect to the server
            Socket client = new Socket(serverName, port);

            // Get output stream to send data to the server
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            // Get input stream to receive data from the server
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            // Send CONNECT message to server
            out.writeUTF("CONNECT");

            // Wait for confirmation from server
            String confirmation = in.readUTF();
            if (!"CONNECTED".equals(confirmation)) {
                System.out.println("Failed to connect to server.");
                client.close();
                return;
            }

            // Send client name to server
            out.writeUTF(clientName);

            // Read server name from server
            String serverIdentity = in.readUTF();

            // Create a thread to read messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        // Read message from server
                        String response = in.readUTF();
                        System.out.println(serverIdentifier+catch (IOException e) {
                    System.out.println("Connection closed by server.");
                }
            });

            // Start the thread
            readThread.start();

            // Main thread to send messages to the server
            while (true) {
                System.out.print(clientName + " is typing: "); // Display client's name
                String message = scanner.nextLine();
                // Send message to server in uppercase
                out.writeUTF(message.toUpperCase());
                if (message.equalsIgnoreCase("CLOSE")) {
                    break;
                }
            }

            // Close the connection and scanner
            client.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
