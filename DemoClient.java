import java.io.*;
import java.net.*;
import java.util.*;

public class DemoClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = sc.nextLine();

        int myPort = new Random().nextInt(2000) + 10000;

        // Start peer listener
        new PeerListener(myPort).start();

        // Connect to directory server
        Socket server = new Socket("127.0.0.1", 9000);
        BufferedReader sin = new BufferedReader(
                new InputStreamReader(server.getInputStream()));
        PrintWriter sout = new PrintWriter(
                server.getOutputStream(), true);

        // Register
        sout.println(username);
        sout.println(myPort);

        System.out.print("Chat with (username): ");
        String target = sc.nextLine();

        // Lookup peer
        sout.println(target);

        String ip = sin.readLine();
        if (ip.equals("OFFLINE")) {
            System.out.println("User not online");
            return;
        }

        int port = Integer.parseInt(sin.readLine());

        // Direct P2P connection
        Socket peer = new Socket(ip, port);
        PrintWriter peerOut = new PrintWriter(
                peer.getOutputStream(), true);

        System.out.println("Private chat connected with " + target);

        while (true) {
            String msg = sc.nextLine();
            peerOut.println(username + " (private): " + msg);
        }
    }
}



 class PeerListener extends Thread {

    ServerSocket ss;

    public PeerListener(int port) throws IOException {
        ss = new ServerSocket(port);
        System.out.println("Listening on port " + port);
    }

    public void run() {
        while (true) {
            try {
                Socket peer = ss.accept();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(peer.getInputStream()));

                String msg;
                while ((msg = in.readLine()) != "End") {
                    System.out.println("\n" + msg);
                }
                peer.close();
            } catch (Exception ignored) {
            }
        }
    }
}
