
import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends AbstractUser implements Runnable {

    private static volatile List<String> clients = new ArrayList<>();

  List<String> result_games = new ArrayList<>();

    ServerSocket ss;

    Client client;
    public Server(String[] args) {
        super(args);

    }

    public Server(Client client) { //client which is starts to listen
        super(client);
        this.client = client;

        clients.addAll(client.getClients());
        result_games.addAll(Client.result_games);

    }

    @Override
    public void run() {

        try {

            //wait fro another player
            System.out.println("new Connection from: " + socket.getRemoteSocketAddress());

            ServerGameProtocol sgp = new ServerGameProtocol(this,clients,result_games);

            sgp.playGameServer();

            System.out.println("Played games: ");





        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void runServer() {


        try {


            ss = new ServerSocket(MyPort);

            while (true) {

                socket = new Socket();


                System.out.println(this.toString());

                socket = ss.accept();

                //create streams
                instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outstream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


                String msg = instream.readLine();
                if (msg.equals("JOIN")) {

                    System.out.println("JOIN from: " + socket.getRemoteSocketAddress());

                    //send list of people
                    if (clients.size() == 0) {
                        outstream.write(this.toString() + "\n");
                        outstream.flush();
                    } else {
                        outstream.write(this.toString() + "\n");
                        outstream.flush();
                        for (int i = 0; i < clients.size(); i++) {
                            outstream.write(clients.get(i) + "\n");
                            outstream.flush();
                        }
                    }

                    clients.forEach(System.out::println);

                    socket.close();

                } else if(msg.equals("PLAY")){



                    Thread th = new Thread(this);
                    th.start();
                    th.join();
                    result_games.forEach(System.out::println);
                }
                else  if (msg.contains("DELETE")){
                    System.out.println("Requst for delete from: " + socket.getRemoteSocketAddress());
                    for(int i = 0; i < clients.size();i++) {
                        if (clients.get(i).split(" ")[1].equals(msg.split(" ")[2])) {
                            System.out.println("Removing player from list: " + msg.split(" ")[2]);
                            clients.remove(i);

                        }
                    }

                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public List<String> getClients() {
        return clients;
    }

    public List<String> getResult_games() {
        return result_games;
    }
}
