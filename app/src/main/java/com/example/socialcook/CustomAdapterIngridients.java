package com.example.socialcook;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.classes.User;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class CustomAdapterIngridients extends RecyclerView.Adapter<CustomAdapterIngridients.MyViewHolder>{

    private Map<String , Integer> dataSet;
    MainPage mainPage;
    String roomID;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewName;
        Button saveButton;
        TextView textInput;
        TextView typeSpecifier;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.saveButton = (Button) itemView.findViewById(R.id.saveButton);
            this.textInput = (TextView) itemView.findViewById(R.id.itemValue);
            this.typeSpecifier = (TextView) itemView.findViewById(R.id.typeSpecifier);
        }


    }

    public CustomAdapterIngridients(Map<String , Integer> data , MainPage mainPage , String roomID) {
        this.dataSet = data;
        this.mainPage = mainPage;
        this.roomID = roomID;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_ingridients, parent, false);

        view.setOnTouchListener(RoomInfo.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final String key = (new ArrayList<>(dataSet.keySet())).get(listPosition);
        String value = Objects.requireNonNull(dataSet.get(key).toString());
        TextView textViewName = holder.textViewName;
        final TextView textInput = holder.textInput;
        final TextView typeSpecifier = holder.typeSpecifier;
        CardView cardView = holder.cardView;
        final Button buttonSave = holder.saveButton;
        final FirebaseDatabase database = FireBase.getDataBase();
        final DatabaseReference amountRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeAmount");
        final DatabaseReference gramsRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeG");
        final DatabaseReference mlRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeML");
        textViewName.setText(key);
        textInput.setText(value);
        amountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(key)) {
                    typeSpecifier.setText("Amount");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gramsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(key)) {
                    typeSpecifier.setText("Grams");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mlRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(key)) {
                    typeSpecifier.setText("ML");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    amountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(key)) {
                                amountRef.child(key).setValue(Integer.parseInt(textInput.getText().toString()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    gramsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(key)) {
                                gramsRef.child(key).setValue(Integer.parseInt(textInput.getText().toString()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    mlRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(key)) {
                                mlRef.child(key).setValue(Integer.parseInt(textInput.getText().toString()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                catch (Exception NullPointerException) {
                    System.out.println("du hello");
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
