package com.example.socialcook;

import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.socialcook.afterlogin.recipeInfoFrag.RecipeInfo;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.classes.Room;
import com.example.socialcook.classes.User;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CustomAdapterIngridients extends RecyclerView.Adapter<CustomAdapterIngridients.MyViewHolder>{

    RoomInfo recipeInfoPage;
    private Map<String , Integer> dataSet;
    MainPage mainPage;
    String roomID;
    String recipeName;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewName;
        Button saveButton;
        TextView textInput;
        TextView typeSpecifier;
        TextView maxAmount;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.saveButton = (Button) itemView.findViewById(R.id.saveButton);
            this.textInput = (TextView) itemView.findViewById(R.id.itemValue);
            this.typeSpecifier = (TextView) itemView.findViewById(R.id.typeSpecifier);
            this.maxAmount = (TextView) itemView.findViewById(R.id.itemRequired); // this variable contains the value of the max amount of each item
        }


    }

    public CustomAdapterIngridients(Map<String, Integer> data, MainPage mainPage, String roomID, String recipeName) {
        this.dataSet = data;
        this.mainPage = mainPage;
        this.roomID = roomID;
        this.recipeName = recipeName;
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
        final String key = (new ArrayList<>(dataSet.keySet())).get(listPosition); // IMPORTANT!!!!
        String value = Objects.requireNonNull(dataSet.get(key).toString());
        final DatabaseReference myRef = FireBase.getDataBase().getReference("recipes");
        TextView textViewName = holder.textViewName;
        final TextView textInput = holder.textInput;
        final TextView typeSpecifier = holder.typeSpecifier;
        final CardView cardView = holder.cardView;
        final Button buttonSave = holder.saveButton;
        final TextView maxAmount = holder.maxAmount;
        final FirebaseDatabase database = FireBase.getDataBase();
        final DatabaseReference recipeRef = database.getReference("recipes").child(recipeName);
        final DatabaseReference amountRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeAmount");
        final DatabaseReference gramsRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeG");
        final DatabaseReference mlRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeML");
        textViewName.setText(key);
        textInput.setText(value);
        amountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            /*
            This snapshot contains :
            1)datasnapshot1 -> here we fill all of the items of hashtable that contain Amounts
            2)datasnapshot2 -> recipeRef belongs to the original item quantities we use it in order to compare to the immidiate text in order to display if the amount is correct
             */
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {
                if(dataSnapshot1.hasChild(key)) {
                    typeSpecifier.setText("Amount");
                    recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            final Recipe recipe = dataSnapshot2.getValue(Recipe.class);
                            Log.d("ROSH","Recipe amount from DB = "+recipe.getRecipeAmount().get(key)+"\nRecipe amount from text is "+textInput.getText().toString());
                            if(recipe.getRecipeAmount().containsKey(key)) {
                                Log.d("AfterMath" , recipe.getRecipeAmount().get(key).toString());
                                maxAmount.setText(recipe.getRecipeAmount().get(key).toString());
                                int recipeValue = Integer.parseInt(recipe.getRecipeAmount().get(key).toString());
                                int InputValue = 0;
                                if (!textInput.getText().toString().matches("")) {
                                    InputValue = Integer.parseInt(textInput.getText().toString());
                                }
                                else {
                                    InputValue = 0;
                                }
                                if(InputValue != recipeValue) {
                                    maxAmount.setTextColor(Color.RED);

                                }
                                else {
                                    maxAmount.setTextColor(Color.WHITE);
                                }
                            }
                            Log.d("DIE KVAR" , ""+Integer.parseInt(dataSnapshot1.child(key).getValue().toString()));
                            textInput.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    int recipeValue = Integer.parseInt(recipe.getRecipeAmount().get(key).toString());
                                    int InputValue = 0;
                                    if (!textInput.getText().toString().matches("")) {
                                        InputValue = Integer.parseInt(textInput.getText().toString());
                                    }
                                    if(InputValue > recipeValue) {
                                        Log.d("MIKE" , "YESSSS!!!!");
                                        buttonSave.setClickable(false);
                                        buttonSave.setAlpha(0.5f);
                                        maxAmount.setTextColor(Color.RED);

                                    }
                                    else {
                                        buttonSave.setClickable(true);
                                        buttonSave.setAlpha(1f);
                                        maxAmount.setTextColor(Color.WHITE);
                                    }
                                    if (InputValue != recipeValue) {
                                        maxAmount.setTextColor(Color.RED);
                                    }
                                    else {
                                        maxAmount.setTextColor(Color.WHITE);
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            int recipeValue = Integer.parseInt(recipe.getRecipeAmount().get(key).toString());
                            int roomValue = Integer.parseInt(dataSnapshot1.child(key).getValue().toString());
                            if(recipeValue == roomValue) {
                                //enter here...........................................................
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*
            This snapshot contains :
            1)datasnapshot3 -> gramsRef refers to the current quantity of hashtable grams that was stored in the room database
         */
        gramsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                if(dataSnapshot3.hasChild(key)) {
                    typeSpecifier.setText("Grams");
                    recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Recipe recipe = dataSnapshot.getValue(Recipe.class);
                            Log.d("GUY",key);
                            Log.d("YUVAL" , recipe.getRecipeName());
                            if(recipe.getRecipeG().containsKey(key)) {
                                Log.d("AfterMath" , recipe.getRecipeG().get(key).toString());
                                maxAmount.setText(recipe.getRecipeG().get(key).toString());
                                //if(recipe.getRecipeG().get(key) == )
                                int recipeValue = Integer.parseInt(recipe.getRecipeG().get(key).toString());
                                int InputValue = 0;
                                if (!textInput.getText().toString().matches("")) {
                                    InputValue = Integer.parseInt(textInput.getText().toString());
                                }
                                else {
                                    InputValue = 0;
                                }
                                if(InputValue != recipeValue) {
                                    maxAmount.setTextColor(Color.RED);

                                }
                                else {
                                    maxAmount.setTextColor(Color.WHITE);
                                }
                            }
                            textInput.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    int recipeValue = Integer.parseInt(recipe.getRecipeG().get(key).toString());
                                    int InputValue = 0;
                                    if (!textInput.getText().toString().matches("")) {
                                        InputValue = Integer.parseInt(textInput.getText().toString());
                                    }
                                    if(InputValue > recipeValue) {
                                        Log.d("MIKE" , "YESSSS!!!!");
                                        buttonSave.setClickable(false);
                                        buttonSave.setAlpha(0.5f);
                                        maxAmount.setTextColor(Color.RED);

                                    }
                                    else {
                                        buttonSave.setClickable(true);
                                        buttonSave.setAlpha(1f);
                                        maxAmount.setTextColor(Color.WHITE);
                                    }
                                    if (InputValue != recipeValue) {
                                        maxAmount.setTextColor(Color.RED);
                                    }
                                    else {
                                        maxAmount.setTextColor(Color.WHITE);
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*
            This snapshot contains :
            1)datasnapshot4 -> mlRef refers to the current quantity of hashtable ML that was stored in the room database
         */
        mlRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {
                if(dataSnapshot4.hasChild(key)) {
                    typeSpecifier.setText("ML");
                    recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Recipe recipe = dataSnapshot.getValue(Recipe.class);
                            Log.d("GUY",key);
                            Log.d("YUVAL" , recipe.getRecipeName());
                            if(recipe.getRecipeML().containsKey(key)) {
                                Log.d("AfterMath" , recipe.getRecipeML().get(key).toString());
                                maxAmount.setText(recipe.getRecipeML().get(key).toString());

                                int recipeValue = Integer.parseInt(recipe.getRecipeML().get(key).toString());
                                int InputValue = 0;
                                if (!textInput.getText().toString().matches("")) {
                                    InputValue = Integer.parseInt(textInput.getText().toString());
                                }
                                else {
                                    InputValue = 0;
                                }
                                if(InputValue != recipeValue) {
                                    maxAmount.setTextColor(Color.RED);

                                }
                                else {
                                    maxAmount.setTextColor(Color.WHITE);
                                }
                            }
                            textInput.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    int recipeValue = Integer.parseInt(recipe.getRecipeML().get(key).toString());
                                    int InputValue = 0;
                                    if (!textInput.getText().toString().matches("")) {
                                        InputValue = Integer.parseInt(textInput.getText().toString());
                                    }
                                    if(InputValue > recipeValue) {
                                        Log.d("MIKE" , "YESSSS!!!!");
                                        buttonSave.setClickable(false);
                                        buttonSave.setAlpha(0.5f);
                                        maxAmount.setTextColor(Color.RED);

                                    }
                                    else {
                                        buttonSave.setClickable(true);
                                        buttonSave.setAlpha(1f);
                                        maxAmount.setTextColor(Color.WHITE);
                                    }
                                    if (InputValue != recipeValue) {
                                        maxAmount.setTextColor(Color.RED);
                                    }
                                    else {
                                        maxAmount.setTextColor(Color.WHITE);
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
        // Once we click on the save button of each row this function will be executed
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