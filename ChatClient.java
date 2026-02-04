import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatClient{

   public static void main(String[] args) throws Exception{

       System.out.println("Client Signing ON");

       try {
           Socket soc = new Socket("127.0.0.1",9090);
           Connection conn = new Connection(soc);
           conn.start();
           System.out.println("Client says connection established");

           BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
           System.out.print("Enter your name: ");
           String name = r.readLine();
           conn.getNos().println(name);

           String message = r.readLine();
           while(!message.equals("End")){
                conn.getNos().println(message);
                message = r.readLine();
           }
           conn.getNos().println("End");
           System.err.println("Client Singing Off");

       } catch (ConnectException e) {
       }
   } 
}

class L1 implements ActionListener{
  
   private JTextField tf;
   private PrintWriter nos;

   public L1(JTextField tf,PrintWriter nos){
       this.tf = tf;
       this.nos = nos;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
       String str =  tf.getText();
       tf.setText("");
       nos.println(str);
   }
  
}

class ChatWindow extends JFrame{

    JTextArea ta;
    JTextField tf;
    JButton b1;
    JPanel p;

    public ChatWindow(Connection conn,String username){
       super(username+"'s Chat"); 
       ta = new JTextArea();
       ta.setEditable(false);
       tf = new JTextField(15);
       b1 = new JButton("Send");
       ActionListener al = new L1(tf,conn.getNos());
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

class Connection extends Thread{
      Socket soc;
      PrintWriter nos;
      BufferedReader nis;
      
      public Connection(Socket soc) throws IOException{ 
        this.soc = soc; 
        nos = new PrintWriter(soc.getOutputStream(), true);
        nis = new BufferedReader(new InputStreamReader(soc.getInputStream()));      
      }

      public void run() {
        try {
            
            String str= nis.readLine();
            while(!str.equals("End")){
                System.out.println(str);
                str = nis.readLine();
            }

        } catch (Exception e) {
        }
        
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