package tokyproduction.volleystats;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Registry extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, android.widget.PopupMenu.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {

    Button buL;
    Button buR ;
    TextView tvTime;
    Button buCh;

    private boolean team1point;
    private String time ="00:00:00";
    private String winningTeam = "";
    private String cause ="-";
    private String causedPlayer="-";
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference dbTeam1 = database.getReference("Team1");
    DatabaseReference dbTeam2 = database.getReference("Team2");

    Game myGame = new Game();
    StopWatch stpWatch = new StopWatch();
    final int REFRESH_RATE = 100;
    final int MSG_START_TIMER =0;
    final int MSG_STOP_TIMER =1;
    final int MSG_UPDATE_TIMER = 2;
    final int TEAMONE =1;
    final int TEAMTWO =2;
    Boolean switchside = false;


    // Beispielcode zum Hinzufügen von Team und Spieler
    Team team1;

    Team team2;

    public Registry(){


    }

    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    stpWatch.start();
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;
                case MSG_UPDATE_TIMER:
                    tvTime.setText(stpWatch.toString());
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, REFRESH_RATE);
                    break;
                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER);
                    stpWatch.stop();
                    tvTime.setText(stpWatch.toString());
                    break;
                default:
                    break;


            }
        }

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        buL = (Button) findViewById(R.id.buttonL);
        tvTime =(TextView) findViewById(R.id.tvTime);

        buR = (Button) findViewById(R.id.buttonR);
        buCh = (Button) findViewById(R.id.btnChange);


        buL.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        time = (String) tvTime.getText();
                        pointreceiving(TEAMONE);

                    }
                }
            );

        buR.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        time = (String) tvTime.getText();
                        pointreceiving(TEAMTWO);



                    }
                }
        );
        buL.setOnLongClickListener(
                new Button.OnLongClickListener(){
                    public boolean onLongClick(View v){

                        time = (String) tvTime.getText();
                        team1point =true;
                        showPopup(v);

                        return true;

                    }
                }
        );

        buR.setOnLongClickListener(
                new Button.OnLongClickListener(){
                    public boolean onLongClick(View v){
                        time = (String) tvTime.getText();
                        team1point =false;
                        showPopup(v);


                        return true;

                    }
                }
        );
        buCh.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        //Todo Auswechsel Fenster öffnen

                    }
                }
        );

        dbTeam1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                team1 = dataSnapshot.getValue(Team.class);
                String value = dataSnapshot.getValue(String.class);
                Log.d("Team 1", "Value is "+value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbTeam2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                team2 = dataSnapshot.getValue(Team.class);
                String value = dataSnapshot.getValue(String.class);
                Log.d("Team2", "Value is "+value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        startNewGame();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        actualizeView();
    }
    private void resetPointItems(){
        time ="00:00:00";
        winningTeam = "";
        cause ="-";
        causedPlayer="-";
    }




    private void pointreceiving(int button){


        if(button==1 ||button==2){
            if(button==1){
                if(!switchside){
                    winningTeam = team1.getTeamname();
                    myGame.pointRegister(team1,team2,this);
                }else{
                    winningTeam = team2.getTeamname();
                    myGame.pointRegister(team2,team1, this);
                }
            }else{
                if(!switchside){
                    winningTeam = team2.getTeamname();
                    myGame.pointRegister(team2,team1,this);
                }else{
                    winningTeam = team1.getTeamname();
                    myGame.pointRegister(team1,team2,this);
                }

            }


        }
        myGame.setPointItems(time,winningTeam,cause,causedPlayer);

        resetPointItems();



    }
    private void showPopup(View v){
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(Registry.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.specify_menu,popup.getMenu());
        popup.show();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startNewGame();
            return true;
        }
        if(id == R.id.swich_Item){
            setSwitchside();

        }
        if (id == R.id.mnUndo){
            myGame.undo();
        }

        return super.onOptionsItemSelected(item);
    }
    private void restartGame(){
        mHandler.sendEmptyMessage(MSG_STOP_TIMER);
        mHandler.sendEmptyMessage(MSG_START_TIMER);
        myGame.reset();
        team1.reset();
        team2.reset();
        myGame.addTeam(team1);
        myGame.addTeam(team2);
        actualizeView();
    }
    private void startNewGame(){
        AlertDialog.Builder builderStart = new AlertDialog.Builder(Registry.this);

        builderStart.setTitle("Start");
        builderStart.setPositiveButton("Das Spiel Starten!", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mHandler.sendEmptyMessage(MSG_STOP_TIMER);
                mHandler.sendEmptyMessage(MSG_START_TIMER);
            }
        });



        builderStart.show();



        team1.reset();
        team1.addPlayer("Beispielspieler 1", 03,"Angriff");
        team2.reset();
        team2.addPlayer("Gegner", 03,"Angriff");
        myGame.reset();
        myGame.addTeam(team1);
        myGame.addTeam(team2);
        actualizeView();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.load_Team) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.new_Team) {



        } else if (id == R.id.mnshow_Stats) {
            Intent intent = new Intent(this, stats.class);

            intent.putExtra("Points",myGame.getPoints());



            startActivity(intent);

        } else if (id == R.id.mnsettings) {
            Intent intent = new Intent(this, SettingsActivity.class );
            startActivity(intent);


        } else {
            if (id == R.id.mnteam1) {


                Intent intentTeam = new Intent(this, newTeam.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("TeamName", team1.getTeamname());
                bundle.putSerializable("Players",team1.getSpieler());

                bundle.putSerializable("Team",TEAMONE);



                intentTeam.putExtras(bundle);



                startActivityForResult(intentTeam,1);



            } else if (id == R.id.mnteam2) {
                Intent intentTeam = new Intent(this, newTeam.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("TeamName", team2.getTeamname());
                bundle.putSerializable("Players",team2.getSpieler());

                bundle.putSerializable("Team",TEAMONE);


                intentTeam.putExtras(bundle);




                startActivityForResult(intentTeam,2);

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Button 1 pressed

    public void setSwitchside(){
        if(!switchside){
            switchside = true;
        }else{
            switchside=false;
        }
        Toast.makeText(this,"Seiten werden gewechselt",Toast.LENGTH_LONG).show();
        actualizeView();
    }





    public void actualizeView(){


        //Punkte Team1
        TextView teLP = new TextView(this);
        teLP =(TextView) findViewById(R.id.textView10);

        //Punkte Team 2
        TextView teRP = new TextView(this);
        teRP =(TextView) findViewById(R.id.textView12);

        //Saetze Team Links
        TextView teLS = new TextView(this);
        teLS =(TextView) findViewById(R.id.textView4);

        // Saetze Team 2
        TextView teRS = new TextView(this);
        teRS =(TextView) findViewById(R.id.textView6);

        // Aktueller Satz
        TextView te5 = new TextView(this);
        te5 =(TextView) findViewById(R.id.textView8);

        //setContentView(R.layout.table_layout);


        // Teamnamen

        te5.setText(Integer.toString(myGame.getActualSet()));

        if(!switchside){
            buR.setText("Punkt für "+team2.getTeamname());

            buL.setText("Punkt für "+team1.getTeamname());

            teLP.setText(Integer.toString(team1.getPoints()));
            teRP.setText(Integer.toString(team2.getPoints()));
            teLS.setText(Integer.toString(team1.getWinningSets()));
            teRS.setText(Integer.toString(team2.getWinningSets()));
        }else{
            buR.setText("Punkt für "+team1.getTeamname());

            buL.setText("Punkt für "+team2.getTeamname());

            teLP.setText(Integer.toString(team2.getPoints()));
            teRP.setText(Integer.toString(team1.getPoints()));
            teLS.setText(Integer.toString(team2.getWinningSets()));
            teRS.setText(Integer.toString(team1.getWinningSets()));



        }



    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {




        boolean winnerteam=true;
        switch (item.getItemId()){

            case R.id.mnAnspielfehler:
                Toast.makeText(getBaseContext(), "Ein Anspielfehler von ",Toast.LENGTH_SHORT );
                cause = "Anspielfehler";
                winnerteam=false;
                break;

            case R.id.mnAss:
                Toast.makeText(getBaseContext(), "Ein Ass von ",Toast.LENGTH_SHORT );
                cause = "Ass";
                break;

            case R.id.mnNetzBeruhrung:
                Toast.makeText(getBaseContext(), "Eine Netzberührung von ",Toast.LENGTH_SHORT );
                cause = "Netzberührung";
                winnerteam=false;
                break;

            case R.id.mnTechFehler:
                Toast.makeText(getBaseContext(), "Ein Technischer Fehler von ",Toast.LENGTH_SHORT );
                cause = "Technischer Fehler";
                winnerteam=false;
                break;

            default:

                return false;

        }



        if(winnerteam){
            if (team1point){
                askforPerson(1);
            }else {
                askforPerson(2);
            }

        }else {
            if (team1point){
                askforPerson(2);
            }else {
                askforPerson(1);
            }

        }


        return true;

    }

    private void askforPerson(int teampoint) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Registry.this);

        builderSingle.setTitle("Verursacher:");


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Registry.this, android.R.layout.select_dialog_singlechoice);
        if(teampoint==1&& !switchside){
            for(Player player : team1.getSpieler()){
                if (player.isInGame()){
                    arrayAdapter.add(player.getPlayer());
                }

            }

        }else{
            for(Player player : team2.getSpieler()){
                if (player.isInGame()){
                    arrayAdapter.add(player.getPlayer());
                }
            }
        }



        builderSingle.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (team1point){
                    pointreceiving(TEAMONE);
                }else {
                    pointreceiving(TEAMTWO);
                }

                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                causedPlayer = strName;
                if (team1point){
                    pointreceiving(TEAMONE);
                }else {
                    pointreceiving(TEAMTWO);
                }

                }

        });
        builderSingle.show();
    }


    protected void onActivityResult(int requestcode, int resultCode, Intent data){


        Bundle bundle1 = data.getExtras();
        ArrayList<Player> actualTeam = (ArrayList<Player>) bundle1.getSerializable("Team");
        String TeamName = (String) bundle1.getSerializable("TeamName");

        if(requestcode==1){
            team1.setNewTeam(actualTeam);
            team1.setTeamname(TeamName);
        }else{
            team2.setNewTeam(actualTeam);
            team2.setTeamname(TeamName);
        }
        restartGame();
    }




}
