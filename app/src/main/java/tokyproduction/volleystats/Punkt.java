package tokyproduction.volleystats;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Marco on 05.05.2017.
 */

public class Punkt implements Serializable{
    private String ts;
    private String Team;
    private String Grund;
    private String Spieler;


    public Punkt(String time, String team, String caused, String causedPlayer){

        this.ts = time;
        this.Team = team;
        this.Grund = caused;
        this.Spieler = causedPlayer;
    }
    public String getTs(){
        return ts;
    }


    public String getTeam() {
        return Team;
    }

    public String getGrund() {
        return Grund;
    }

    public String getSpieler() {
        return Spieler;
    }



}
