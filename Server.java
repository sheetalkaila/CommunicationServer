import java.io.*;
import java.net.*;

public class Server{
   public static void main(String[] args)throws Exception{
       System.out.println("Server Signing ON");
       ServerSocket ss = new ServerSocket(9083);
       Socket soc = ss.accept();
       System.out.println("Server Says Connection Established");
       BufferedReader nis = new BufferedReader(
                             new InputStreamReader(
                               soc.getInputStream()
                             )
                           );

        PrintWriter nos = new PrintWriter(
                           new BufferedWriter(
                                new OutputStreamWriter(
                                  soc.getOutputStream()
                                )
                           ),true
                         );

       String str = nis.readLine();

       while(!str.equals("End")){
           System.out.println("Server Recieved " + str);
           nos.println("Server Ack " + str);
           str = nis.readLine();
       }
       nos.println("End");
       System.out.println("Server Singing OFF");
   }
}