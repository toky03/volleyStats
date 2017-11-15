package tokyproduction.volleystats;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;




public class newTeam extends AppCompatActivity {

    private ArrayList<Player> Players = new ArrayList<>();
    private String strTeam;
    private Button btnSave;
    private Button btnNewPlayer;
    private Game actualGame;
    private int TEAM;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_new_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnNewPlayer =(Button) findViewById(R.id.btnAddPlayer);




        final EditText teamName = (EditText) findViewById(R.id.tfTeamName);

        Intent intent = this.getIntent();

        final Bundle bundle = intent.getExtras();

        strTeam = (String) bundle.getSerializable("TeamName");
        Players = (ArrayList<Player>) bundle.getSerializable("Players");

        TEAM =(int) bundle.getSerializable("Team");

        teamName.setText(strTeam);
        actualizeView();




        btnSave.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        strTeam = teamName.getText().toString();
                        Intent intent1 = new Intent();
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("Team",Players);
                        bundle1.putSerializable("TeamName",strTeam);
                        intent1.putExtras(bundle1);
                        setResult(1,intent1);
                        finish();




                    }
                }
        );

        btnNewPlayer.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Player player = new Player(0);
                        editPlayer("newPlayer",player);


                    }
                }
        );

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

    private void editPlayer(String newPlayer,Player plrPlayer ) {
        final Dialog alertDialogBuilder = new Dialog(newTeam.this);
        alertDialogBuilder.setContentView(R.layout.custom_dialog);

        final EditText etname = (EditText) alertDialogBuilder.findViewById(R.id.etName);
        final EditText etposition = (EditText) alertDialogBuilder.findViewById(R.id.etPosition);
        final EditText etnumber = (EditText)  alertDialogBuilder.findViewById(R.id.etNummer);
        final CheckBox etcheckBox = (CheckBox) alertDialogBuilder.findViewById(R.id.cbInGame);
        Button btnSave = (Button) alertDialogBuilder.findViewById(R.id.btnSave);

        if(newPlayer=="newPlayer"){

            alertDialogBuilder.setTitle("Spieler hinzufügen");
        }else  {
            alertDialogBuilder.setTitle("Spieler ändern");
            etname.setText(plrPlayer.getPlayer());
            etposition.setText(plrPlayer.getPosition());
            etnumber.setText(Integer.toString(plrPlayer.getNumber()));
            etcheckBox.setChecked(plrPlayer.isInGame());

        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = etname.getText().toString();
                int intNumber = Integer.parseInt(etnumber.getText().toString());
                String strPosition = etposition.getText().toString();
                Boolean boolCorePlayer = etcheckBox.isChecked();
                Player newPlayer = new Player(strName, intNumber, strPosition);
                if (boolCorePlayer){
                    newPlayer.setInGame();
                }


                Players.add(newPlayer);
                alertDialogBuilder.dismiss();
                actualizeView();

            }
        });


        Button btnCancel = (Button) alertDialogBuilder.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.cancel();
            }
        });

        alertDialogBuilder.show();


    }


    private void actualizeView(){
        final ListAdapter adapter = new CustomAdapterPlayer(this, Players);

        final ListView ListViewPlayers = (ListView) findViewById(R.id.lvPlayers);
        ListViewPlayers.setClickable(true);
        ListViewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

                Player player = (Player) ListViewPlayers.getItemAtPosition(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(newTeam.this);
                alertDialogBuilder.setTitle("Spieler Ändern");
                alertDialogBuilder.setCancelable(true)
                        .setPositiveButton("Spieler Ändern",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                Player plr = Players.get(position);
                                Players.remove(position);
                                actualizeView();
                                editPlayer("Edit",plr);

                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton("Spieler löschen",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Players.remove(position);
                                actualizeView();
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });

        ListViewPlayers.setAdapter(adapter);
    }




}
