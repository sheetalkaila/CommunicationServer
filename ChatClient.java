
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatClient {

    public static void main(String[] args) throws Exception {

        System.out.println("Client Signing ON");
        Socket soc = new Socket("127.0.0.1", 9090);
        Connection conn = new Connection(soc);

        String username = JOptionPane.showInputDialog("Enter your username:");
        conn.getNos().println(username);

        ChatWindow cw = new ChatWindow(conn, username);
        conn.setChatWindow(cw);
        conn.start();
    }
}

class L1 implements ActionListener {

    private JTextField tf;
    private PrintWriter nos;

    public L1(JTextField tf, PrintWriter nos) {
        this.tf = tf;
        this.nos = nos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = tf.getText();
        tf.setText("");
        nos.println(str);
    }

}

class ChatWindow extends JFrame {

    JTextArea ta;
    JTextField tf;
    JButton b1;
    JPanel p;

    public ChatWindow(Connection conn, String username) {
        super(username + "'s Chat");
        ta = new JTextArea();
        ta.setEditable(false);
        tf = new JTextField(15);
        b1 = new JButton("Send");
        ActionListener al = new L1(tf, conn.getNos());
        b1.addActionListener(al);
        tf.addActionListener(al);
        p = new JPanel();
        p.add(tf);
        p.add(b1);
        add(p, BorderLayout.SOUTH);
        add(ta);
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    JTextArea getTa() {
        return ta;
    }

}

class Connection extends Thread {

    Socket soc;
    PrintWriter nos;
    BufferedReader nis;
    ChatWindow cw;

    public Connection(Socket soc) throws IOException {
        this.soc = soc;
        nos = new PrintWriter(soc.getOutputStream(), true);
        nis = new BufferedReader(new InputStreamReader(soc.getInputStream()));
    }

    public void setChatWindow(ChatWindow cw) {
        this.cw = cw;
    }

    public void run() {
        try {

            String str = nis.readLine();
            while (!str.equals("End")) {
                System.out.println(str);
                cw.getTa().append(str + "\n");
                str = nis.readLine();
            }
            soc.close();
            cw.dispose();
            System.out.println("Connection Closed");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public PrintWriter getNos() {
        return nos;
    }

    public BufferedReader getNis() {
        return nis;
    }
}
