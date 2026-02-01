
import java.io.*;
import java.net.*;
import javax.swing.*;

public class SerialServer {

    public static void main(String[] args) throws Exception {

        System.out.println("Server signing ON");
        ServerSocket ss = new ServerSocket(9085);
        ChatWindow cw = new ChatWindow();
        handleClient(cw,ss);
        System.out.println("Server Singing OFF");
    }

    public static void handleClient(ChatWindow cw, ServerSocket ss) throws Exception {

        for (int i = 1; i <= 5; i++) {

            Socket soc = ss.accept();
            Connection conn = new Connection(soc);

            String userName = conn.getNis().readLine();
            String str = conn.getNis().readLine();

            while (!str.equals("End")) {
                cw.getTa().append("\nServer Received From " + userName + " : " + str);
                conn.getNos().println(str);
                str = conn.getNis().readLine();
            }
            String userLogout = conn.getNis().readLine();
            cw.getTa().append("\n" + userName + ": " + userLogout);

        }
    }

}

class ChatWindow extends JFrame {

    private JTextArea ta;

    public ChatWindow() {
        ta = new JTextArea();
        ta.setEditable(false);
        setTitle("Serial Server");
        add(ta);
        setLocation(800, 200);
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTextArea getTa() {
        return ta;
    }
}

class Connection {

    Socket soc;
    PrintWriter nos;
    BufferedReader nis;

    public Connection(Socket soc) throws Exception {
        this.soc = soc;
        nos = new PrintWriter(soc.getOutputStream(), true);
        nis = new BufferedReader(
                new InputStreamReader(
                        soc.getInputStream()
                )
        );
    }

    public Socket getsoc() {
        return soc;
    }

    public PrintWriter getNos() {
        return nos;
    }

    public BufferedReader getNis() {
        return nis;
    }
}
