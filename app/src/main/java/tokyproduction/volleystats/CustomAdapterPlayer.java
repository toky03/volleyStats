package tokyproduction.volleystats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Marco on 05.06.2017.
 */

public class CustomAdapterPlayer extends BaseAdapter {

    ArrayList<Player> Players = new ArrayList<>();
    Context context;


    public CustomAdapterPlayer(Context context,ArrayList<Player> player) {
        this.context =context;
        this.Players =player;
    }

    @Override
    public int getCount() {
        return Players.size();
    }

    @Override
    public Object getItem(int position) {
        return Players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View rowViewPlayer = inflater.inflate(R.layout.custom_row_player,parent,false);

        Player actualPlayer = Players.get(position);

        String strName = actualPlayer.getPlayer();
        String strPosition = actualPlayer.getPosition();
        Boolean inGame = actualPlayer.isInGame();
        String strPlayerStat;
        if (inGame){
            strPlayerStat= "Stammspieler";
        }else{
            strPlayerStat="Kein Stammspieler";
        }
        int intNumber = actualPlayer.getNumber();

        TextView Name = (TextView) rowViewPlayer.findViewById(R.id.colPlayer);
        TextView Position = (TextView) rowViewPlayer.findViewById(R.id.colPosition);
        TextView Number = (TextView) rowViewPlayer.findViewById(R.id.colNumber);
        TextView Status =(TextView) rowViewPlayer.findViewById(R.id.colCorePlayer);

        Name.setText(strName);
        Position.setText(strPosition);
        Number.setText(Integer.toString(intNumber));
        Status.setText(strPlayerStat);


        return rowViewPlayer;
    }


}
