package com.mambobryan.travels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TravelsListActivity extends AppCompatActivity {

    private FloatingActionButton mAddFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Toolbar dealsToolbar = findViewById(R.id.dealsToolbar);
        setSupportActionBar(dealsToolbar);

        mAddFab = findViewById(R.id.add_floatingActionButton);
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(TravelsListActivity.this,
                        TravelDealEditActivity.class);
                startActivity(myIntent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        invalidateOptionsMenu();
        inflater.inflate(R.menu.list_activity_menu, menu);
        if (FirebaseUtil.isAdmin) {
            menu.findItem(R.id.new_travel_deal_menu).setVisible(true);
            mAddFab.setVisibility(View.VISIBLE);
        } else {
            menu.findItem(R.id.new_travel_deal_menu).setVisible(false);
            mAddFab.setVisibility(View.INVISIBLE);
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
