import java.io.*;
import java.net.*;

public class Server{
   public static void main(String[] args)throws Exception{
       System.out.println("Server Signing ON");
       ServerSocket ss = new ServerSocket(9090);
       Socket soc = ss.accept();
       Connection conn = new Connection(soc);
       System.out.println("Server Says Connection Established");
       handleClient(conn);
       System.out.println("Server Singing OFF");
      }

      public static void handleClient(Connection conn)throws Exception{
        String str = conn.getNis().readLine();
        while(!str.equals("End")){
           System.out.println("Server Recieved " + str);
           conn.getNos().println("Server Ack " + str);
           str = conn.getNis().readLine();
       }
      conn.getNos().println("End");
      }
}

    class Connection{
      Socket soc;
      PrintWriter nos;
      BufferedReader nis;
      
      public Connection(Socket soc) throws Exception{
        this.soc = soc;
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