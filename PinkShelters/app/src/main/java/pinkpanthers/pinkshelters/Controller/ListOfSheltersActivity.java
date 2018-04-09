package pinkpanthers.pinkshelters.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

import pinkpanthers.pinkshelters.Model.DBI;
import pinkpanthers.pinkshelters.Model.Db;
import pinkpanthers.pinkshelters.Model.Shelter;
import pinkpanthers.pinkshelters.R;


public class ListOfSheltersActivity extends AppCompatActivity
        implements RecyclerAdapter.ItemClickListener, View.OnClickListener {

    private List<Shelter> shelters;
    private String username; //used to get current logged in user

    /**
     * Create a list view of all the shelters
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        // data to populate the RecyclerView with
        ArrayList<String> shelterNames = new ArrayList<>();

        DBI db = new Db("pinkpanther", "PinkPantherReturns!");

        shelters = db.getAllShelters();
        for (int i = 0; i < shelters.size(); i++) {
            Shelter s = shelters.get(i);
            shelterNames.add(s.getShelterName());
        }

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvShelters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter adapter = new RecyclerAdapter(this, shelterNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // set up search button
        Button search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        username = extras != null ? extras.getString("username") : null;
    }

    /**
     * Direst to detail page when a shelter is selected
     * @param view current view
     * @param position current position of item clicked
     */
    @Override
    public void onItemClick(View view, int position) { //clicked on one shelter
        Intent detail = new Intent(this, ShelterDetails.class);
        Shelter s = shelters.get(position);
        detail.putExtra("shelterId", s.getId());
        detail.putExtra("username", username);
        startActivity(detail);
    }

    /**
     * Direct to map
     * @param view View
     */
    public void showMapButton(@SuppressWarnings("unused") View view) {
        Intent map = new Intent(this, MapsActivity.class);
        map.putExtra("username", username);
        startActivity(map);
    }

    /**
     * Direct to search page
     * @param v current view
     */
    @Override
    public void onClick(View v) { //search button
        Intent intent = new Intent(ListOfSheltersActivity.this, SearchActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}