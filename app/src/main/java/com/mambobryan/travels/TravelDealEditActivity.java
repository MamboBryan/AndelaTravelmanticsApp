package com.mambobryan.travels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class TravelDealEditActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private static final int PICTURE_RESULT = 42;
    private static final String LOG_TAG = TravelDealEditActivity.class.getName();

    EditText title_TV;
    EditText description_TV;
    EditText price_TV;
    ImageView image_IV;

    private TravelDeal mNewDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        title_TV = findViewById(R.id.txtTitle);
        description_TV = findViewById(R.id.txtDescription);
        price_TV = findViewById(R.id.txtPrice);
        image_IV = findViewById(R.id.imageView);

        Intent myIntent = getIntent();

        TravelDeal selectedDealFromList = (TravelDeal) myIntent.getSerializableExtra("deal");
        if (selectedDealFromList == null) {
            selectedDealFromList = new TravelDeal();
        }

        this.mNewDeal = selectedDealFromList;

        title_TV.setText(mNewDeal.getTitle());
        price_TV.setText(mNewDeal.getPrice());
        description_TV.setText(mNewDeal.getDescription());
        showImage(selectedDealFromList.getImageUrl());

        Button uploadImageButton = findViewById(R.id.buttonUploadImage);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageUploadIntent = new Intent(Intent.ACTION_GET_CONTENT);
                imageUploadIntent.setType("image/jpeg");
                imageUploadIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(imageUploadIntent.createChooser(imageUploadIntent,
                        "Select Picture"), PICTURE_RESULT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.deal_activity_menu, menu);

        if (FirebaseUtil.isAdmin) {
            menu.findItem(R.id.delete_menu).setVisible(true);
            menu.findItem(R.id.save_menu).setVisible(true);
            findViewById(R.id.buttonUploadImage).setEnabled(true);
            enableEditTexts(true);
        } else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            findViewById(R.id.buttonUploadImage).setEnabled(false);
            enableEditTexts(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "Deal saved", Toast.LENGTH_SHORT).show();
                cleanDeal();
                backToTravelList();
                return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal deleted", Toast.LENGTH_SHORT).show();
                backToTravelList();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            StorageReference ref =
                    FirebaseUtil.mStorageReference.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String url = task.getResult().toString();
                                            mNewDeal.setImageUrl(url);
                                            mNewDeal.setImageName(taskSnapshot.getStorage().getPath());
                                            showImage(url);
                                        }
                                    });

                        }
                    });
        }
    }

    private void cleanDeal() {

        title_TV.setText("");
        price_TV.setText("");
        description_TV.setText("");

        title_TV.requestFocus();

    }

    private void saveDeal() {
        mNewDeal.setTitle(title_TV.getText().toString());
        mNewDeal.setPrice(price_TV.getText().toString());
        mNewDeal.setDescription(description_TV.getText().toString());

        if (mNewDeal.getId() == null) {
            mDatabaseReference.push().setValue(mNewDeal);
        } else {
            mDatabaseReference.child(mNewDeal.getId()).setValue(mNewDeal);
        }
    }

    private void deleteDeal() {
        if (mNewDeal.getId() == null) {
            Toast.makeText(this, "Please save deal before deleting",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseReference.child(mNewDeal.getId()).removeValue();
        if (mNewDeal.getImageName() != null && !mNewDeal.getImageName().isEmpty() ){
            StorageReference picRef = FirebaseUtil.mFirebaseStorage.getReference().child(mNewDeal.getImageName());
            picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }

    }

    private void backToTravelList() {
        Intent myIntent = new Intent(this, TravelsListActivity.class);
        startActivity(myIntent);
    }

    private void enableEditTexts(boolean isEnabled) {
        title_TV.setEnabled(isEnabled);
        price_TV.setEnabled(isEnabled);
        description_TV.setEnabled(isEnabled);
    }

    private void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(screenWidth, screenWidth * 2 / 3)
                    .centerCrop()
                    .into(image_IV);
        }
    }
}
