package com.mambobryan.travels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.mambobryan.travels.FirebaseUtil.attachFirebaseListener;

public class TravelsListActivity extends AppCompatActivity {

    private static final String LOG_TAG = "FirebaseTrav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        invalidateOptionsMenu();
        inflater.inflate(R.menu.list_activity_menu, menu);
        if (FirebaseUtil.isAdmin) {
            menu.findItem(R.id.new_travel_deal_menu).setVisible(true);
        } else {
            menu.findItem(R.id.new_travel_deal_menu).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_travel_deal_menu:
                Intent myIntent = new Intent(this, TravelDealEditActivity.class);
                startActivity(myIntent);
                return true;

            case R.id.log_out_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseUtil.attachFirebaseListener();
                            }
                        });
                FirebaseUtil.detachFirebaseListener();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachFirebaseListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Toast.makeText(this, "RESUMED", Toast.LENGTH_LONG).show();
        FirebaseUtil.openFirebaseReference("traveldeals", this);
        RecyclerView recyclerViewDeals = findViewById(R.id.recyler_deals);

        TravelDealAdapter adapter = new TravelDealAdapter();
        recyclerViewDeals.setAdapter(adapter);

        LinearLayoutManager dealsLayoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewDeals.setLayoutManager(dealsLayoutManager);

        FirebaseUtil.attachFirebaseListener();

    }

    public void showMenu() {
        this.invalidateOptionsMenu();
    }
}
