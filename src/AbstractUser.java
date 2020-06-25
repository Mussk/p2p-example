import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class AbstractUser {

    protected String nickname;

    protected String MyIP = "";

    protected String IPserver;

    protected Integer portServer;

    protected Integer MyPort;

    protected BufferedWriter outstream;

    protected BufferedReader instream;

   protected static volatile Socket socket;

    public AbstractUser(String[] args){
        try {
            if (args.length == 3) { //client
                this.nickname = args[0];
                this.MyIP = InetAddress.getLocalHost().getHostAddress();
                this.MyPort = (int)(1024 + Math.random() *10000);
                this.IPserver = args[1];
                this.portServer = Integer.parseInt(args[2]);


            } else { //server
                this.nickname = args[0];
                this.MyIP = InetAddress.getLocalHost().getHostAddress();
                this.MyPort = Integer.parseInt(args[1]);

            }
        }catch (Exception ehx) {ehx.printStackTrace();}
    }

    public AbstractUser(Client client){
            this.nickname = client.nickname;
            this.MyIP = client.MyIP;
            this.MyPort = client.MyPort;
            this.socket = client.socket;



    }

    @Override
    public String toString() {
        return  "Username: " + nickname + " IP: " + MyIP + " FreePort: " + MyPort;
        }

    }

