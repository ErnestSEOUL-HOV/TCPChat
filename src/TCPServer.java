import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    ServerSocket serverSocket;
    Socket clientSocket;
    int port;
    boolean exit;

    public TCPServer() {
        port = 8081;
        exit = false;
    }

    public class ReceiveThread extends Thread {
        /*PrintWriter in;*/
        BufferedReader stdIn;

        public ReceiveThread() throws IOException {

            /* in = new PrintWriter(clientSocket.getOutputStream(), true);*/
            stdIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        public void run() {
            String fromUser;
            while (true) {
                try {
                    if (exit) break;
                    fromUser = stdIn.readLine();
                    if (fromUser.equals("@exit")) {
                        System.out.println("user disconnected!");
                        exit = true;
                        break;
                    }
                    if (fromUser.length() >= 4) {
                        if (!fromUser.substring(0, 4).equals("@cat")) {
                            System.out.println("From user: " + fromUser);
                        }
                    } else if (fromUser.length() >= 3) {
                        if (!fromUser.substring(0, 3).equals("@ls")) {
                            System.out.println("From user: " + fromUser);
                        }
                    } else {
                        System.out.println("From user: " + fromUser);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class SendThread extends Thread {
        PrintWriter out;
        BufferedReader stdIn;


        public SendThread() throws IOException {

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        }

        public void run() {
            String fromServer;
            while (true) {
                try {
                    if (exit) break;
                    fromServer = stdIn.readLine();
                    out.println(fromServer);
                    if (fromServer.equals("@exit")) {
                        System.out.println("you are disconnected!");
                        exit = true;
                        break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start() throws Exception {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        SendThread st = new SendThread();
        ReceiveThread rt = new ReceiveThread();

        rt.start();
        st.start();
        System.out.println("connected");
        st.join();
        System.exit(0);

    }

    public static void main(String[] args) throws Exception {
        TCPServer server = new TCPServer();
        server.start();
    }

}





