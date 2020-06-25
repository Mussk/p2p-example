import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class ListProtocol implements Runnable {

   private InputStream client; //another person

   private List<String> clients;

    public ListProtocol(InputStream instream, List<String> clients) {

        this.clients = clients;

        this.client = instream;

    }

    @Override
    public void run() {

        Scanner s = new Scanner(client);
        String tmp = "";
        while (s.hasNextLine()) {
            tmp = s.nextLine();
            System.out.println("Insert record to the array");
            clients.add(tmp);
        }


    }

    public List<String> getClients() {
        return clients;
    }
}