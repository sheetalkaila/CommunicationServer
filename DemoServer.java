import java.io.*;
import java.net.*;
import java.util.*;

public class DemoServer {

    static Map<String, PeerInfo> users =
            Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(9000);
        System.out.println("Directory Server running on port 9000");

        while (true) {
            Socket soc = ss.accept();
            new ClientHandler(soc).start();
        }
    }
}

class PeerInfo {
    String ip;
    int port;

    PeerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}

class ClientHandler extends Thread {
    Socket soc;

    ClientHandler(Socket soc) {
        this.soc = soc;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(
                    soc.getOutputStream(), true);

            String username = in.readLine();
            int port = Integer.parseInt(in.readLine());

            DemoServer.users.put(username,
                    new PeerInfo(soc.getInetAddress().getHostAddress(), port));

            System.out.println(username + " registered");

            while (true) {
                String target = in.readLine();
                PeerInfo p = DemoServer.users.get(target);

                if (p == null) {
                    out.println("OFFLINE");
                } else {
                    out.println(p.ip);
                    out.println(p.port);
                }
            }

        } catch (Exception ignored) {
        }
    }
}
