import javax.lang.model.element.NestingKind;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;

public class Client extends AbstractUser implements Runnable{

    private String[] args;

    private List<String> clients = new ArrayList<>();

    private ServerSocket serverSocket;

   static List<String> result_games = new ArrayList<>();




    public Client(String[] args){
        super(args);
    }


    @Override
    public void run(){


        for (int i = 0; i < clients.size(); i++) {

           ClientGameProtocol  cgp =  new ClientGameProtocol(this,clients.get(i),i,clients,result_games);

            cgp.playGameClient();


        }
        System.out.println("Played Games: ");

        result_games.forEach(System.out::println);

    }

    public void runClient(){

        try {

            socket = new Socket();

            //connecting to ListServer
            socket.connect(new InetSocketAddress(IPserver, portServer));

            //creating streams
            PrintWriter outstream = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            outstream.write("JOIN" + "\n");
            outstream.flush();

            ClientProtocol cp = new ClientProtocol(socket.getInputStream());

            //read list of players
            Thread th = new Thread(cp);
            th.start();
            th.join(250);

            clients.addAll(cp.getClients());

            System.out.println("Received list of users: ");

            clients.forEach(System.out::println);



            //start playing game

            new Thread(this).start();



            System.out.println("Generated port: " + MyPort);

            System.out.println("Start Listenig...");

            new Server(this).runServer();


        }catch (Exception ex){ex.printStackTrace();}


    }


    public List<String> getResult_games() {
        return result_games;
    }

    public List<String> getClients() {
        return clients;
    }
}
