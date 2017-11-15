package tokyproduction.volleystats;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Marco on 05.05.2017.
 */

class Team implements Serializable{

    private String teamname;
    private int points;
    private int winningSets;
    private ArrayList<Player> Spieler = new ArrayList<>();
    private ArrayList<Integer> lastPoint = new ArrayList<>();



    Team(String name){
        this.points=0;
        this.teamname = name;
        this.winningSets = 0;
    }
    void PointsToZero(){
        lastPoint.add(points);
        points =0;
    }

    void setLastPoint(){
        points = lastPoint.get(lastPoint.size()-1);
        lastPoint.remove(lastPoint.size()-1);

    }
    void setLastSet(){
        winningSets -=1;
    }
    public  void setNewTeam(ArrayList<Player> newPlayers){
        Spieler.clear();
        Spieler.addAll(newPlayers);
    }

    public String getTeamname(){
        return teamname;
    }
    public int getPoints(){
        return points;
    }
    public int getWinningSets(){
        return winningSets;
    }
    public void winningPoints(){
        lastPoint.add(points);
        points +=1;
    }
    public void setTeamname(String name){
        teamname =name;
    }
    public void reset(){
        points =0;
        winningSets =0;
    }
    public void addPlayer(String spieler, int number,String position){
        Player player = new Player(spieler,number,position);
        Spieler.add(player);
    }

    public ArrayList<Player> getSpieler(){
        return Spieler;
    }
    public void wonSet(){
        winningSets +=1;
    }

}
