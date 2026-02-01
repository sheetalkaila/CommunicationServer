
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class SerialClient {

    public static void main(String[] args) throws Exception {

        System.out.println("Client Signing ON");
        Socket soc = new Socket("127.0.0.1", 9085);
        Connection conn = new Connection(soc);
        PrintWriter nos = conn.getNos();
        BufferedReader nis = conn.getNis();
        ChatWindow cw = new ChatWindow(conn);

       
        String str = nis.readLine();

        while (!str.equals("End")) {
            cw.getTa().append(str + "\n");
            str = nis.readLine();
        }

        cw.getTa().append("\nClient Signing Off\n");
    }
}

class ChatWindow extends JFrame {

    JTextArea ta;
    JTextField tf;
    JButton b1;
    JPanel p;

    public ChatWindow(Connection conn) {
        ta = new JTextArea();
        ta.setEditable(false);
        setTitle("GUI Echo Client");
        tf = new JTextField(15);
        b1 = new JButton("Send");
        ActionListener al = new L1(ta, tf, conn.getNos());
        b1.addActionListener(al);
        tf.addActionListener(al);
        p = new JPanel();
        p.add(tf);
        p.add(b1);
        add(ta);
        add(BorderLayout.SOUTH, p);
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTextArea getTa() {
        return ta;
    }

    public JTextField getTf() {
        return tf;
    }
}

class L1 implements ActionListener {

    private JTextArea ta;
    private JTextField tf;
    private PrintWriter nos;

    public L1(JTextArea ta, JTextField tf, PrintWriter nos) {
        this.ta = ta;
        this.tf = tf;
        this.nos = nos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String str = tf.getText();
        nos.println(str);
        tf.setText("");

        if (str.equals("End")) {
            nos.close();
            System.exit(0);
        }
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
