package com.example.socialcook.afterlogin.mainPageFrag;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.firebase.FireBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private static final String TAG = "imageURL";
    private ArrayList<Recipe> dataSet;
    MainPage mainPage;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        String url;
        TextView textViewName;
        ImageView imageURL;
        ProgressBar progressBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageURL = (ImageView) itemView.findViewById(R.id.imageID);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }


    }

    public CustomAdapter(ArrayList<Recipe> data , MainPage mainPage) {
        this.dataSet = data;
        this.mainPage = mainPage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnTouchListener(MainPageFrag.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final TextView textViewName = holder.textViewName;
        final CardView cardView = holder.cardView;
        final ImageView image = holder.imageURL;
        final ProgressBar progressBar = holder.progressBar;
        String url =holder.url;
        if (dataSet.get(listPosition).getImageUrl().startsWith("images/")) {
            FireBase.storageRef.child(dataSet.get(listPosition).getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide
                            .with(cardView.getContext())
                            .load(uri)
                            .centerCrop()
                            .placeholder(progressBar.getProgressDrawable())
                            .into(image);
                    textViewName.setText(dataSet.get(listPosition).getRecipeName());
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mainPage.loadRecipePage(dataSet.get(listPosition));
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        else {
            Glide
                    .with(cardView.getContext())
                    .load(dataSet.get(listPosition).getImageUrl())
                    .centerCrop()
                    .placeholder(progressBar.getProgressDrawable())
                    .into(image);
            textViewName.setText(dataSet.get(listPosition).getRecipeName());
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainPage.loadRecipePage(dataSet.get(listPosition));
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
