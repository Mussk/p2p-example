import java.io.*;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ServerGameProtocol extends AbstractGameProtocol implements Runnable {

  private  Server server;

  private Socket socket;

  private BufferedWriter outstream;

  private  BufferedReader instream;

  private List<String> clients;

  private  List<String> result_games;

    public ServerGameProtocol(Server server, List<String> clients,List<String> result_games) {
        this.server = server;
        this.socket = server.socket;
        this.generated_num = (int)(1 + Math.random() * 10);
        this.outstream = server.outstream;
        this.instream = server.instream;
        this.clients = clients;

        this.result_games = result_games;

    }

    @Override
    public void run() {

        try {
            Socket socketrun = new Socket();
            System.out.println("In thread");
            BufferedReader command = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                if (command.readLine().equals("QUIT")) {

                    System.out.println("Quitting...");

                    System.out.println("Size: " + clients.get(0));
                    for (int i = 0; i < clients.size(); i++) {
                        socketrun.connect(new InetSocketAddress(clients.get(i).split(" ")[3],
                                Integer.parseInt(clients.get(i).split(" ")[5])));
                        BufferedWriter outstream = new BufferedWriter(new OutputStreamWriter(socketrun.getOutputStream()));
                        outstream.write("DELETE " + server + "\n");
                        outstream.flush();
                        System.out.println(clients.get(i));
                    }

                    System.out.println("Played games: ");
                    for (int i = 0; i < result_games.size(); i++) {
                        System.out.println(result_games.get(i));
                    }
                    System.exit(0);
                }
            }

        }catch (Exception ex){ex.printStackTrace();}
        System.out.println("thread quit ends");
    }

    public void playGameServer(){

        try {
            //quit
           new Thread(this).start();
            //receive generated number and record, g_id
            System.out.println("recieved number");

            String tmp = instream.readLine();
           received_num = Integer.parseInt(tmp.split("~")[0]);
            String record = tmp.split("~")[1];
          g_id = tmp.split("~")[2];

          server.getClients().add(record);


          //send generated number
            System.out.println("send gen num");
            outstream.write(generated_num + "\n");
            outstream.flush();
            System.out.println(generated_num);

            //calculate sum
            sum = generated_num + received_num;

            //send sum
            System.out.println("send sum");
            outstream.write(sum + "\n");
            outstream.flush();

            //recieve sum
            System.out.println("recieve sum");
            String tnp = instream.readLine();
            received_sum = Integer.parseInt(tnp);



            //compare sum with recieved sum
            if (sum.equals(received_sum)) {
                System.out.println("send OK");
                outstream.write("OK" + "\n");
                outstream.flush();
                if (instream.readLine().equals("OK")) {

                    //generate who starts

                    Integer start = (int)(1 + Math.random());

               result = playGame(record.split(" ")[1],server.nickname,start,sum);

              server.getResult_games().add(result);



              //send result i g_id
                    outstream.write(result + "~" + g_id + "\n");
                    outstream.flush();

                    socket.close();

                } else {
                    System.out.println("Bye!");
                    System.exit(0);
                }
            } else {
                outstream.write("NOT OK" + "\n");
                outstream.flush();
            }




        }catch (Exception ex){ex.printStackTrace();}
    }

}
