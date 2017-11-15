package tokyproduction.volleystats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class stats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ArrayList<Punkt> punkte = new ArrayList<Punkt>();
        Intent i = getIntent();

        punkte = (ArrayList<Punkt>) getIntent().getSerializableExtra("Points");



        ListAdapter adapter = new CustomAdapter(this, punkte);

        ListView marcosListview = (ListView) findViewById(R.id.lvStatistics);
        marcosListview.setAdapter(adapter);
    }



}
