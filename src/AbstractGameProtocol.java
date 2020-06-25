import java.util.UUID;

public abstract class AbstractGameProtocol  {


    protected String g_id;

    protected Integer generated_num,

                        received_num,
                        sum,
                        received_sum;

    protected volatile String result;

    public AbstractGameProtocol() {


    }

    public String getResult(){

        return result;
    }

    public String playGame (String player1,String player2,Integer gen_num,Integer sum){

        String winner = "";

        if (gen_num == 1) {
            //currentUser starts
            if (sum % 2 == 0)
                winner = player2;
            else winner = player1;

        } else if(gen_num == 2) {
            //Inviter starts
            if (sum % 2 == 0)
                winner = player1;
            else winner = player2;
        }

        return player1 + " vs " + player2 + ". Winner: " + winner;


    }

    public String generate_g_id(){

        return UUID.randomUUID().toString();
    }
}
