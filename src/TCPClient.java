import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    Socket socket;
    String hostname;
    int port;
    boolean exit;

    public TCPClient() {
        port = 8081;
        hostname = "localhost";
        exit = false;
    }

    public class ReceiveThread extends Thread {
        BufferedReader in;

        public ReceiveThread() throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        }

        public void run() {
            String fromServer;
            while (true) {
                try {
                    if (exit) break;
                    fromServer = in.readLine();
                    if (fromServer.equals("@exit")) {
                        System.out.println("server disconnected!");
                        exit = true;
                        break;
                    }
                    System.out.println("From server: " + fromServer);
                } catch (Exception e) {
                }
            }

        }
    }

    public class SendThread extends Thread {
        PrintWriter out;
        BufferedReader stdIn;

        public SendThread() throws Exception {
            out = new PrintWriter(socket.getOutputStream(), true);
            stdIn = new BufferedReader(new InputStreamReader(System.in));

        }

        public void run() {
            String fromUser;
            while (true) {
                try {
                    if (exit) break;
                    fromUser = stdIn.readLine();
                    out.println(fromUser);
                    if (fromUser.equals("@exit")) {
                        System.out.println("you are disconnected!");
                        exit = true;
                        break;
                    }
                    if (fromUser.length() >= 4) {
                        if (fromUser.substring(0, 4).equals("@cat")) {
                            try {
                                String fileName = fromUser.substring(5);
                                BufferedReader br = new BufferedReader(new FileReader("E:\\Рабочий стол\\Laba11DOP\\src\\" + fileName));
                                for (String line; (line = br.readLine()) != null; ) {
                                    System.out.print(line);
                                }
                                br.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (fromUser.equals("@ls")) {
                        File folder = new File("E:\\Рабочий стол\\Laba11DOP\\src");
                        File[] listOfFiles = folder.listFiles();

                        for (File listOfFile : listOfFiles) {
                            if (listOfFile.isFile()) {
                                System.out.println("File " + listOfFile.getName());
                            } else if (listOfFile.isDirectory()) {
                                System.out.println("Directory " + listOfFile.getName());
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start() throws Exception {
        socket = new Socket(hostname, port);
        SendThread st = new SendThread();
        ReceiveThread rt = new ReceiveThread();

        rt.start();
        st.start();
        System.out.println("connected");
        st.join();
        System.exit(0);

    }

    public static void main(String[] args) throws Exception {
        TCPClient client = new TCPClient();
        client.start();
    }

}


