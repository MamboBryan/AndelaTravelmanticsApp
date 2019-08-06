package com.mambobryan.travels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TravelDealAdapter extends RecyclerView.Adapter<TravelDealAdapter.TravelDealViewHolder> {

    private ArrayList<TravelDeal> mDeals;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ChildEventListener mChildEventListener;

    private ImageView mDealImageView;

    private static Activity caller;

    private static final String LOG_TAG = TravelDealAdapter.class.getName();

    public TravelDealAdapter() {

        //FirebaseUtil.openFirebaseReference("traveldeals");

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        mDeals = FirebaseUtil.mDeals;

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TravelDeal gottenDeal = dataSnapshot.getValue(TravelDeal.class);
                Log.i(LOG_TAG, "Deal title: " + gottenDeal.getTitle());
                gottenDeal.setId(dataSnapshot.getKey());
                mDeals.add(gottenDeal);

                notifyItemInserted(mDeals.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public TravelDealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.recycler_list_item, parent, false);

        return new TravelDealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelDealViewHolder holder, int position) {

        TravelDeal deal = mDeals.get(position);
        holder.bind(deal);

    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }

    public class TravelDealViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;

        public TravelDealViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.textView_recycler_title);
            tvDescription = itemView.findViewById(R.id.textView_recycler_description);
            tvPrice = itemView.findViewById(R.id.textView_recycler_price);
            mDealImageView = itemView.findViewById(R.id.imageView_recycler_image);

            itemView.setOnClickListener(this);
        }

        public void bind(TravelDeal deal) {
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
            showImage(deal.getImageUrl());
        }

        @Override
        public void onClick(View view) {
            int dealPosition = getAdapterPosition();
            Log.i(LOG_TAG, "Clicked : " + String.valueOf(dealPosition));

            TravelDeal selectedTravelDeal = mDeals.get(dealPosition);

            Intent myIntent = new Intent(view.getContext(), TravelDealEditActivity.class);
            myIntent.putExtra("deal", selectedTravelDeal);
            view.getContext().startActivity(myIntent);
        }

        private void showImage(String url){
            if (url != null && !url.isEmpty()){
                Picasso.get()
                        .load(url)
                        .resize(160, 160)
                        .centerCrop()
                        .into(mDealImageView);
            }
        }
    }
}