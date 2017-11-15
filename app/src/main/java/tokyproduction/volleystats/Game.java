package tokyproduction.volleystats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import static android.R.attr.button;

/**
 * Created by Marco on 05.05.2017.
 */

public class Game implements Serializable{
    private int actualSet;
    private ArrayList<Team> teams = new ArrayList<Team>();
    private ArrayList<Punkt> Points = new ArrayList<Punkt>();
    Registry mainApp;
    private ArrayList<String> lastAction = new ArrayList<String>();


    final String INCREASE_SET = "IncreaseSet";
    final String SINGLEPOINT ="TeamPoint";

    private ArrayList<Team> lastWinningTeam = new ArrayList<>();
    private ArrayList<Team> lastLoosingTeam = new ArrayList<>();


    public Game(){
        this.actualSet=1;

    }


    public Team getTeam1(){
        return teams.get(0);
    }
    public Team getTeam2(){
        return teams.get(1);
    }
    public void undo(){
        Points.remove(Points.size()-1);

        switch (lastAction.get(lastAction.size()-1)){
            case INCREASE_SET:
                if(actualSet!=1){
                    actualSet -=1;
                    lastWinningTeam.get(lastWinningTeam.size()-1).setLastPoint();
                    lastWinningTeam.get(lastWinningTeam.size()-1).setLastSet();
                    lastLoosingTeam.get(lastLoosingTeam.size()-1).setLastPoint();

                    lastWinningTeam.remove(lastWinningTeam.size()-1);
                    lastLoosingTeam.remove(lastLoosingTeam.size()-1);

                    lastAction.remove(lastAction.size()-1);

                    mainApp.actualizeView();


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainApp);
                    alertDialogBuilder.setMessage("Wollen Sie die Seite zurückwechseln?");
                            alertDialogBuilder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Toast.makeText(mainApp,"Seiten werden gewechselt",Toast.LENGTH_LONG).show();
                                            mainApp.setSwitchside();
                                        }
                                    });

                    alertDialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
                break;
            case SINGLEPOINT:
                lastWinningTeam.get(lastWinningTeam.size()-1).setLastPoint();
                lastWinningTeam.remove(lastWinningTeam.size()-1);
                lastAction.remove(lastAction.size()-1);


                mainApp.actualizeView();
                break;
            default:
                mainApp.actualizeView();
                break;



        }







    }
    public ArrayList<Punkt> getPoints(){
        return Points;
    }
    public void reg(Punkt p){

        Points.add(p);
    }
    public void addTeam(Team team){
        teams.add(team);
    }
    public void reset(){
        actualSet=1;
        Points.clear();
       // if(!teams.isEmpty()){
         //   teams.clear();
        //}
    }
    public int getActualSet(){
        return actualSet;
    }
    public void increaseSet(){

        actualSet+=1;
    }

    public void setPointItems(String time,String team, String caused, String player){
        Punkt newPoint = new Punkt(time, team, caused, player);
        reg( newPoint);
    }



    public void pointRegister(Team team, Team secondTeam, Registry mainApp){
        this.mainApp =mainApp;
        lastWinningTeam.add(team);
        lastLoosingTeam.add(secondTeam);

        if (actualSet==5){
            setTo15(team, secondTeam);

    }else{
            setTo25(team, secondTeam);
        }

    }

    private void setTo25(Team team, Team secondTeam) {
        int PTeam1= team.getPoints();
        int PTeam2 = secondTeam.getPoints();

        if((PTeam1>=24 || PTeam2 >=24) && Math.abs(PTeam1-PTeam2)>=2 ){

            lastAction.add(INCREASE_SET);
            winningSet(team, secondTeam);
        }else{
            distributePoints(team);
            lastAction.add(SINGLEPOINT);
        }


    }

    private void setTo15(Team team, Team secondTeam){
        int PTeam1= team.getPoints();
        int PTeam2 = secondTeam.getPoints();


        if((PTeam1>=14 || PTeam2 >=14) && Math.abs(PTeam1-PTeam2)>=2 ){
            lastAction.add(INCREASE_SET);
            winningSet(team, secondTeam);


        }else{
            lastAction.add(SINGLEPOINT);
            distributePoints(team);

        }

    }

    private void distributePoints(Team team) {
        team.winningPoints();
        mainApp.actualizeView();
    }

    private void winningSet(Team team, Team loserTeam){

        team.wonSet();
        if(team.getWinningSets()==3){
            team.reset();
            loserTeam.reset();
            reset();


            Toast.makeText(mainApp.getApplicationContext(),"Team "+team.getTeamname()+" hat das Spiel gewonnen!",Toast.LENGTH_SHORT).show();
        }else {
            increaseSet();
            team.PointsToZero();
            loserTeam.PointsToZero();
            mainApp.actualizeView();


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainApp);
            alertDialogBuilder.setMessage("Wollen Sie die Seite wechseln?");
            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    mainApp.setSwitchside();
                }
            });

            alertDialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            Toast.makeText(mainApp.getApplicationContext(), "Team " + team.getTeamname() + " hat den Satz gewonnen!", Toast.LENGTH_SHORT).show();


        }




        }


    }



