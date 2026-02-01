import java.io.*;
import java.net.*;
import javax.swing.*;

public class SerialServer {

    public static void main(String[] args) throws Exception {

        System.out.println("Server signing ON");
        ServerSocket ss = new ServerSocket(9085);

        JFrame f1 = new JFrame("Serial Server");
        f1.setSize(400, 400);
        f1.setLocation(800, 200);
        JTextArea ta = new JTextArea(20, 20);
        ta.setEditable(false);
        f1.add(ta);
        f1.setVisible(true);

        for (int i = 1; i <= 5; i++) {

            Socket soc = ss.accept();

            BufferedReader nis = new BufferedReader(
                    new InputStreamReader(soc.getInputStream())
            );

            PrintWriter nos = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(soc.getOutputStream())
                    ),
                    true
            );

            String userName = nis.readLine();
            String str = nis.readLine();

            while (!str.equals("End")) {
                ta.append("\nServer Received From " + userName + " : " + str);
                nos.println(str);
                str = nis.readLine();
            }

            String userLogout = nis.readLine();
            ta.append("\n" + userName + ": " + userLogout);
        }

        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.out.println("Server signing OFF");
    }
}
