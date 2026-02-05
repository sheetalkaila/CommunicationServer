import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    public static MessageQueue<String> mq = new MessageQueue<>();
    public static ArrayList<PrintWriter> al = new ArrayList<PrintWriter>();

    public static void main(String[] args) throws Exception {
        System.out.println("Server Signing ON");
        ServerSocket ss = new ServerSocket(9090);
        MessageDispatcher md = new MessageDispatcher();
        md.setDaemon(true);
        md.start();

        int clientCount = 1;
        for (int i = 0; i < 10; i++) {

            Socket soc = ss.accept();
            String threadName = "Client-" + clientCount;
            Connection conn = new Connection(soc,threadName);
            conn.start();
            clientCount++;
        }
        System.out.println("Server Singing OFF");
        
    }

}

class MessageQueue<T> {

    ArrayList<T> al = new ArrayList<>();

    synchronized public void enqueue(T item) {
        al.add(item);
        notify();
    }

    synchronized public T dequeue() {

        if (al.isEmpty()) {
            try {
                wait();
            } catch (Exception ex) {

            }
        }
        return al.remove(0);
    }
}

class Connection extends Thread {

    Socket soc;
    String Username;

    public Connection(Socket soc, String threadName) {
        this.soc = soc;
        this.setName(threadName);
    }

    @Override
    public void run() {
        System.out.println("Connection thread " + this.getName() + " sigined On");

        try {

            BufferedReader nis = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter nos = new PrintWriter(soc.getOutputStream(), true);
            ChatServer.al.add(nos);

            Username = nis.readLine();
            System.out.println("Client name: " + Username);

            String message = nis.readLine();
            while (!message.equals("End")) {
                String fullMessage = Username + " : " + message;
                ChatServer.mq.enqueue(fullMessage);
                System.out.println("Server Recieved " + fullMessage);
                message = nis.readLine();
            }
            nos.println("End");
            ChatServer.al.remove(nos);
            System.out.println("Connection with " + soc.getInetAddress().getHostAddress() + " Terminated");

        } catch (Exception e) {

        }
        System.out.println("Connection thread  "+ this.getName() + " signing Off");
    }
}

class MessageDispatcher extends Thread {

    @Override
    public void run() {
        while (true) {
            try {
                String message = ChatServer.mq.dequeue();
                for (PrintWriter p : ChatServer.al) {
                    p.println(message);
                }
            } catch (Exception e) {
            }
        }
    }
}
