import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author
 *  Oleksandr Karpenko, s16934
 * **/

public class MarynazGameV2 {

    public static void main(String[] args) {

        try {
            if (args.length == 2)
                runServer(args);

            else if
                (args.length == 3) runClient(args);

        } catch (Exception ex) {
        }


    }

    public static void runServer(String[] args) throws IOException {

        new Server(args).runServer();
    }


    public static void runClient(String[] args) throws IOException {

        Client c = new Client(args);
        c.runClient();

    }


}
