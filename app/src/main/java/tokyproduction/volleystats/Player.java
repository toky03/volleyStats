package tokyproduction.volleystats;

import java.io.Serializable;

/**
 * Created by Marco on 05.06.2017.
 */

public class Player implements Serializable{
    private String Player;
    private String Position;
    private int number;
    private boolean inGame;




    public Player(String player,int number, String position){
        this.Player=player;
        this.number = number;
        this.Position=position;

    }
    public void setInGame(){
        inGame=true;
    }
    public boolean isInGame(){
        return inGame;
    }
    public void setOutOfGame(){
        inGame=false;
    }


    public Player(String player, int number){
        this(player,number,"Allround");
    }
    public Player(int number){
        this("Unbekannt", number);
        this.inGame =false;
    }

    public String getPlayer(){
        return Player;
    }
    public int getNumber(){
        return number;
    }
    public String getPosition(){
        return  Position;
    }

    public void setPlayer(String name){
        this.Player = name;
    }
    public void setPosition(String position){
        this.Position = position;
    }

    public void setNumber(int number){
        this.number = number;
    }


}
