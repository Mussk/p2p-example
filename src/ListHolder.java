import java.io.*;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;



public class ListHolder implements Runnable{

    volatile static List<String> all_clients = new ArrayList<>();

    Socket socket;

  final static String IP = "localhost";

   final static Integer port = 10800;

    BufferedWriter outstream;

    BufferedReader instream;

    String player1,player2,winner, command;

    public ListHolder() {

        this.socket = new Socket();

    }

    public ListHolder(Socket socket,BufferedReader bf,BufferedWriter bw, String command){
        this.socket = socket;
        this.outstream = bw;
        this.instream = bf;
        this.command = command;
    }

    @Override
    public void run() {
        try {

            /* version without storaging data */

            System.out.println("recieve record");

         //   BufferedWriter outstream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

       //     BufferedReader instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //receive record
            String received_msg = command;
            System.out.println("recieved");

            player1 = received_msg.split("~")[1];

            String IP = received_msg.split("~")[2];

            Integer Port = Integer.parseInt(received_msg.split("~")[3]);

            String g_id = received_msg.split("~")[4];

            player2 = received_msg.split("~")[5];

            Integer sum = Integer.parseInt(received_msg.split("~")[6]);

            Integer gen_num = (int)((1 + Math.random()) * 100);

            String result = playGame(gen_num,sum);

            outstream.write(result + "\n");
            outstream.flush();

            //connecting to another player

           Socket socket2 = new Socket();

            socket2.connect(new InetSocketAddress(IP,Port));

            outstream.write(result + "\n");
            outstream.flush();

            socket2.close();

        }catch (Exception ex) {ex.printStackTrace();}
    }

    public void runListHolder(){


    }

    public String playGame (Integer gen_num,Integer sum){

        if (gen_num > 0 && gen_num <= 50) {
            //currentUser starts
            if (sum % 2 == 0)
                winner = player2;
            else winner = player1;

        } else if(gen_num > 50 && gen_num <= 100) {
            //Inviter starts
            if (sum % 2 == 0)
                winner = player1;
            else winner = player2;
        }

        return player1 + " vs " + player2 + ". Winner: " + winner;





    }

}
