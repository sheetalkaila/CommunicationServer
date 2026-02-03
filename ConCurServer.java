
import java.io.*;
import java.net.*;
import java.util.*;

public class ConCurServer {

    public static ArrayList<PrintWriter> al = new ArrayList<PrintWriter>();

    public static void main(String[] args) throws Exception {
        System.out.println("Server Signing ON");
        ServerSocket ss = new ServerSocket(9090);

        int clientCount = 1;
        for (int i = 1; i < 10; i++) {

            Socket soc = ss.accept();
            String threadName = "Client-" + clientCount;
            Connection conn = new Connection(soc, al, threadName);
            conn.start();
            clientCount++;
        }
        System.out.println("Server Singing OFF");
        ss.close();
    }

}

class Connection extends Thread {

    Socket soc;
    ArrayList<PrintWriter> al;
    String Username;

    public Connection(Socket soc, ArrayList<PrintWriter> al,String threadName) {
        this.soc = soc;
        this.al = al;
        this.setName(threadName);
    }

    @Override
    public void run() {
        System.out.println("Connection thread " + this.getName() + " sigined On");

        try {

            BufferedReader nis = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter nos = new PrintWriter(soc.getOutputStream(), true);
            al.add(nos);
            Username = nis.readLine();
            String message = nis.readLine();
            while (!message.equals("End")) {
                System.out.println("Server Recieved " + message);
                for (PrintWriter p : al) {
                    p.println(Username + " : " + message);
                }
                message = nis.readLine();
            }
            nos.println("End");
            al.remove(nos);

        } catch (Exception e) {

            System.out.println("Client Seems to have abruptly closed the connection");
        }
        System.out.println("Connection thread  "+ this.getName() + " signing Off");
    }
}
