package com.example.socialcook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.classes.User;
import com.example.socialcook.firebase.FireBase;

import java.util.ArrayList;
import java.util.Map;

public class CustomAdapterIngridients extends RecyclerView.Adapter<CustomAdapterIngridients.MyViewHolder>{

    private ArrayList<Map<String, Integer>> dataSet;
    MainPage mainPage;
    private NotificationManagerCompat notificationManager;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewName;
        Button infoButton;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.infoButton = (Button) itemView.findViewById(R.id.buttonSelectRoom);
        }


    }

    public CustomAdapterIngridients(ArrayList<Map<String,Integer>> data , MainPage mainPage) {
        this.dataSet = data;
        this.mainPage = mainPage;
        notificationManager = NotificationManagerCompat.from(mainPage);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_userlist_layout, parent, false);

        view.setOnTouchListener(RoomInfo.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        CardView cardView = holder.cardView;
        final Button buttonInfo = holder.infoButton;

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mainPage.sendNotification(dataSet.get(listPosition).getName());
                //   buttonInfo.setClickable(false);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
