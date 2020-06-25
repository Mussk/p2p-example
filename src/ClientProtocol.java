import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientProtocol implements Runnable {

    private volatile List<String> clients = new ArrayList<>();

    InputStream client;


    public ClientProtocol(InputStream client) {
        this.client = client;

    }

    @Override
    public void run() {

        try {



            //taking list from ListHolder
            Scanner s = new Scanner(client);
            String tmp = "";
            while (s.hasNextLine()) {
                tmp = s.nextLine();
                System.out.println(tmp);
                clients.add(tmp);
            }

            System.out.println("exit from thread");

        }catch (Exception ioex){ioex.printStackTrace();}
    }

    public List<String> getClients() {
        return clients;
    }

}
