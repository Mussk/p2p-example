import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;


public class ClientGameProtocol extends AbstractGameProtocol implements Runnable{

   private Client client;

   private Socket socket;

   private String IPServer;

   private Integer PortServer;

   private int iter;

   private List<String> clients;

   private  List<String> result_games;

    public ClientGameProtocol(Client client, String record, int iter, List<String> clients,List<String> result_games) {
        this.client = client;
        this.socket = client.socket;
        this.IPServer = record.split(" ")[3];
        this.PortServer = Integer.parseInt(record.split(" ")[5]);
        this.generated_num = (int)(1 + Math.random() * 10);
        g_id = generate_g_id();
        this.iter = iter;
       this.clients = clients;
       this.result_games = result_games;
    }

    @Override
    public void run(){
        try {

            Socket socketrun = new Socket();
             BufferedReader command = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                if (command.readLine().equals("QUIT")) {
                    if (iter == clients.size() - 1) {
                        System.out.println("Quitting...");
                        for (int i = 0; i < clients.size(); i++) {
                                socketrun.connect(new InetSocketAddress(clients.get(i).split(" ")[3],
                                        Integer.parseInt(clients.get(i).split(" ")[5])));
                            BufferedWriter outstream = new BufferedWriter(new OutputStreamWriter(socketrun.getOutputStream()));

                            outstream.write("DELETE " + client + "\n");
                                outstream.flush();
                                socketrun.close();
                        }
                        System.out.println("Played games: ");
                        for (int i = 0; i < result_games.size(); i++) {
                            System.out.println(result_games.get(i));
                        }
                        System.exit(0);
                    } else System.out.println("I need to and game with all players first!");
                }

            }
        }catch (Exception ex){ex.printStackTrace();}
    }



    public void playGameClient(){

        try {
            // socket = new Socket();
            socket = new Socket();

            //quitting
            new Thread(this).start();

            //connecting to another player
            socket.connect(new InetSocketAddress(IPServer, PortServer));

            //create streams
            BufferedReader instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter outstream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            outstream.write("PLAY" + "\n");
            outstream.flush();

            System.out.println("Send number");
            //send generated number, record, g_id
            outstream.write(generated_num + "~" + client.toString() + "~" + g_id + "\n");
            outstream.flush();
            //receive generated number
            System.out.println("receive generated number");
            String tmp = instream.readLine();
            received_num = Integer.parseInt(tmp);


            //calculate sum
            sum = generated_num + received_num;

            //receive sum
            System.out.println("recieve sum");
            tmp = instream.readLine();
            received_sum = Integer.parseInt(tmp);

            //send sum
            System.out.println("Send sum");
            outstream.write(sum + "\n");
            outstream.flush();


            //receive accept
            if (instream.readLine().equals("OK")) {
                if (sum.equals(received_sum)) {
                    System.out.println("send OK");
                    outstream.write("OK" + "\n");
                    outstream.flush();

                //read result
                  String tmp1 = instream.readLine();
                  if (tmp1.split("~")[1].equals(g_id))
                    result = tmp1.split("~")[0];

                  client.getResult_games().add(result);

                }  else{

                    outstream.write("NOT OK" + "\n");
                    outstream.flush();
                    socket.close();
                }

            } else {

                System.out.println("Bye!");
                System.exit(0);
            }

        }catch (Exception ex){ ex.printStackTrace();}


    }

}
