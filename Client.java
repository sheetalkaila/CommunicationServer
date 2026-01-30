import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client{

   public static void main(String[] args) throws Exception{

       System.out.println("Client Signing ON");
       Connection conn = new Connection(new EndPoint("127.0.0.1",9090));
       BufferedReader nis = conn.getNis();
       ChatWindow cw = new ChatWindow(conn);
       String str = nis.readLine();
        while(!str.equals("End")){
           cw.getTa().append(str + "\n");
           str = nis.readLine();
       }

       System.out.println("Client Singing OFF");
   } 
}

class L1 implements ActionListener{
  
   private JTextField tf;
   private JTextArea ta;
   private PrintWriter nos;

   L1(JTextField tf, JTextArea ta,PrintWriter nos){
       this.tf = tf;
       this.ta = ta;
       this.nos = nos;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
       String str =  tf.getText();
       tf.setText("");
       nos.println(str);
       if( str.equals("End")){
            nos.close();
            System.exit(1);
       }
   }
  
}

class ChatWindow extends JFrame{

    JTextArea ta;
    JTextField tf;
    JButton b1;
    JPanel p;

    public ChatWindow(Connection conn){
       super("GUI Client"); 
       ta = new JTextArea();
       ta.setEditable(false);
       tf = new JTextField(15);
       b1 = new JButton("Send");
       ActionListener al = new L1(tf,ta,conn.getNos());
       b1.addActionListener(al);
       tf.addActionListener(al);
       p = new JPanel();
       p.add(tf);
       p.add(b1);
       add(p,BorderLayout.SOUTH);
       add(ta);
       setSize(400,400);
       setVisible(true);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    JTextArea getTa(){
        return ta;
    }
       
}

class EndPoint{
    final private String ip;
    final private int port;
    public EndPoint(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }
    public String getIp()
    {
        return ip;
    }
    public int getPort()
    {
        return port;
    }
}

class Connection{
      EndPoint ep;
      Socket soc;
      PrintWriter nos;
      BufferedReader nis;
      
      public Connection(EndPoint ep) throws Exception{
        this.ep = ep;
        soc = new Socket(ep.getIp(),ep.getPort());
        nos = new PrintWriter(soc.getOutputStream(),true);
        nis = new BufferedReader(
                  new InputStreamReader(
                      soc.getInputStream()
                  )
        );
      }
      public Socket getsoc()
      {
        return soc;
      }
      public PrintWriter getNos()
      {
        return nos;
      }
      public BufferedReader getNis()
      {
        return nis;
      }
}