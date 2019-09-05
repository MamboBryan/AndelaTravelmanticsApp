package com.mambobryan.travels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.Currency;
import java.util.Locale;

public class DealViewActivity extends AppCompatActivity {

    private TextView dealTitle;
    private TextView dealPrice;
    private TextView dealDescription;
    private ImageView dealImageView;
    private Button editDealButton;
    private TravelDeal mSelectedDealFromList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_travel_deal);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Toolbar viewDealsToolbar = findViewById(R.id.viewDealToolbar);
        setSupportActionBar(viewDealsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        editDealButton = findViewById(R.id.editDealButton);
        dealTitle = findViewById(R.id.viewDealTitle);
        dealPrice = findViewById(R.id.vieweDealPrice);
        dealDescription = findViewById(R.id.viewDealDescription);

        dealImageView = findViewById(R.id.viewDealImageView);

        Intent myIntent = getIntent();
        mSelectedDealFromList = (TravelDeal) myIntent.getSerializableExtra("deal");

        dealTitle.setText(mSelectedDealFromList.getTitle());
        final Currency currency = Currency.getInstance(Locale.getDefault());
        dealPrice.setText("Price : "
                + currency.getSymbol() + " " + mSelectedDealFromList.getPrice());
        dealDescription.setText(mSelectedDealFromList.getDescription());
        showImage(mSelectedDealFromList.getImageUrl());

        if (!FirebaseUtil.isAdmin) {

            editDealButton.setText("Share");
            editDealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_SUBJECT, mSelectedDealFromList.getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT,
                            mSelectedDealFromList.getDescription() + "\n" +
                                    "Price : "
                                    + currency.getSymbol() + " " + mSelectedDealFromList.getPrice());
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });

        } else {
            editDealButton.setText("edit");
            editDealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(DealViewActivity.this,
                            DealEditActivity.class);
                    myIntent.putExtra("deal", mSelectedDealFromList);
                    startActivity(myIntent);
                }
            });
        }

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
                Intent myIntent = new Intent(this, DealEditActivity.class);
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

    private void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(screenWidth, screenWidth * 2 / 3)
                    .centerCrop()
                    .into(dealImageView);
        }
    }
}
