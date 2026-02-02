
import java.io.*;
import java.net.*;

public class ConCurServer{

    public static void main(String[] args) throws Exception {
        System.out.println("Server Signing ON");
        ServerSocket ss = new ServerSocket(9090);

        for (int i = 1; i < 10; i++) {

            Socket soc = ss.accept();
            Connection conn = new Connection(soc);
            conn.start();
        }

        System.out.println("Server Singing OFF");
    }

}

class Connection extends Thread {

    Socket soc;

    public Connection(Socket soc) {
        this.soc = soc;
    }

    @Override
    public void run() {
        try {

            BufferedReader nis = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter nos = new PrintWriter(soc.getOutputStream(), true);

            String str = nis.readLine();
            while (!str.equals("End")) {
                System.out.println("Server Recieved " + str);
                nos.println("Server Ack " + str);
                str = nis.readLine();
            }

        } catch (Exception e) {

            System.out.println(e);
        }
    }
}
