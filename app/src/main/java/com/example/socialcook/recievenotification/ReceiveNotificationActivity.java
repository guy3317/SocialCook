package com.example.socialcook.recievenotification;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.socialcook.R;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.classes.Room;
import com.example.socialcook.firebase.FireBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReceiveNotificationActivity extends AppCompatActivity {

    private static final String TAG = "DebuggingTogether";
    private static ArrayList<Recipe> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_receive_notification);
        final Button accept = findViewById(R.id.acceptButton);
        final Button reject = findViewById(R.id.rejectButton);
        final DatabaseReference myRef = FireBase.recipeDir;
        Bundle bndl = getIntent().getExtras();
        final DatabaseReference newDir = FireBase.getDataBase().getReference();
        final TextView recipeNameView = findViewById(R.id.recipeNameView);
        final RequestQueue mRequestQue;
        final ImageView recipeImageView = findViewById(R.id.imageRecieve);
        final TextView recipeDescription = findViewById(R.id.description);
        final TextView recipeTypeView = findViewById(R.id.brand);
        final TextView recipeItems = findViewById(R.id.items);
        //final String uidUser = getIntent().getStringExtra("uidSource");/////////////VERY IMPORTANT!
        //final String recipeName = getIntent().getStringExtra("recipeName");
        //final String recipeType = getIntent().getStringExtra("recipeType");
        final String recipeName = bndl.getString("recipeName");
        final String recipeType = bndl.getString("recipeType");
        final String uidUser = bndl.getString("uidSource");
        Log.d(TAG , "UID USER IS "+uidUser);
            mRequestQue = Volley.newRequestQueue(this);
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject json = new JSONObject();
                    Log.d(TAG , "uid on click is "+uidUser);
                    try {
                        Log.d(TAG , "uid user is "+uidUser);
                        if(uidUser == null) {
                            Log.d(TAG , "DAAAA PROBLEEEM IS ----------------------------------------------------------------------------------------------------------------------------------------"+uidUser);
                        }
                        json.put("to","/topics/"+uidUser);
                        JSONObject notificationObj = new JSONObject();
                        notificationObj.put("title","Your request has been accepted!");
                        notificationObj.put("body",FireBase.getAuth().getCurrentUser().getDisplayName()+" just accepted your request");
                        JSONObject extraData = new JSONObject();
                        extraData.put("my_custom_key" , "accept");
                        json.put("notification",notificationObj);
                        json.put("data",extraData);
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FireBase.POST,
                                json,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("MUR", "onResponse: ");
                                        Toast.makeText(getApplicationContext(), "The message was sent successfully",
                                                Toast.LENGTH_SHORT).show();
                                        final Recipe recipe = new Recipe();
                                        final Recipe recipeUid1 = new Recipe();
                                        final Recipe recipeUid2 = new Recipe();
                                        myRef.orderByChild("recipeName").equalTo(recipeName).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot datas: dataSnapshot.getChildren()){
                                                    String recipeNAme=datas.child("recipeName").getValue().toString();
                                                    recipe.setName(recipeName);
                                                    recipeUid1.setName(recipeName);
                                                    recipeUid2.setName(recipeName);
                                                    recipe.setType(recipeType);
                                                    recipeUid1.setType(recipeType);
                                                    recipeUid2.setType(recipeType);
                                                    String recipeTYpe = datas.child("recipeType").getValue().toString();
                                                    for (DataSnapshot child: datas.child("recipeAmount").getChildren()) {
                                                        recipe.setALLRecipeAmount(child.getKey());
                                                        recipeUid1.setALLRecipeAmount(child.getKey());
                                                        recipeUid2.setALLRecipeAmount(child.getKey());
                                                    }
                                                    for (DataSnapshot child: datas.child("recipeG").getChildren()) {
                                                        recipe.setALLRecipeGrams(child.getKey());
                                                        recipeUid1.setALLRecipeGrams(child.getKey());
                                                        recipeUid2.setALLRecipeGrams(child.getKey());
                                                    }
                                                    for (DataSnapshot child: datas.child("recipeML").getChildren()) {
                                                        recipe.setAllRecipeML(child.getKey());
                                                        recipeUid1.setAllRecipeML(child.getKey());
                                                        recipeUid2.setAllRecipeML(child.getKey());
                                                    }
                                                }
                                                newDir.child("rooms").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists()) {
                                                            Room room = new Room();
                                                            String roomId = newDir.child("rooms").push().getKey();
                                                            room.setRecipe(recipe);
                                                            room.setRoomID(roomId);
                                                            room.setUid1(uidUser);
                                                            room.setUid2(FireBase.getAuth().getUid());
                                                            room.setRecipeUid1(recipeUid1);
                                                            room.setRecipeUid2(recipeUid2);
                                                            newDir.child("rooms").child(roomId).setValue(room);
                                                            try {
                                                                FireBase.getDataBase().getReference("users").child(uidUser).child("myRooms").child(roomId).setValue(roomId);
                                                                FireBase.getDataBase().getReference("users").child(FireBase.getAuth().getUid()).child("myRooms").child(roomId).setValue(roomId);
                                                            }
                                                            catch (Exception NullPointerException) {
                                                                Log.d(TAG , "*********FAILED TO SEND BECAUSE THE UID TO SEND TO WAS NULL "+uidUser+"*************");
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("MUR", "onError: "+error.networkResponse);
                            }
                        }
                        ){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> header = new HashMap<>();
                                header.put("content-type","application/json");
                                header.put("authorization","key=AAAA8W53yXc:APA91bHXoE3oXyuCG1GixUIOoy9A8M3EiBbXIF5QH-nrHRTTgZ8l-RSExlX4ALFVnFWFXfGg7YWKZZzPQ9IR_kxksiLDguhRoTBmUfEHGC6qD1UfBTAMalL3WU-MCarVxh36EDCTNG3u");
                                return header;
                            }

                        };
                        mRequestQue.add(request);
                        finish();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
           recipeNameView.setText(recipeType);
            recipeTypeView.setText(recipeName);
            myRef.orderByChild("recipeName").equalTo(recipeName).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot datas: dataSnapshot.getChildren()){
                        String recipeNAme=datas.child("recipeName").getValue().toString();
                        String recipeTYpe = datas.child("recipeType").getValue().toString();
                        String recipeMethod = datas.child("recipeDescription").getValue().toString();
                        String imageUrl = datas.child("imageUrl").getValue().toString();
                        if (imageUrl.startsWith("images/")) {
                            FireBase.storageRef.child(imageUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide
                                            .with(ReceiveNotificationActivity.this)
                                            .load(uri)
                                            .centerCrop()
                                            //.placeholder(progressBar.getProgressDrawable())
                                            .into(recipeImageView);
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
                                    .with(ReceiveNotificationActivity.this)
                                    .load(imageUrl)
                                    .centerCrop()
                                    //    .placeholder(progressBar.getProgressDrawable())
                                    .into(recipeImageView);
                        }
                        recipeNameView.setText(recipeNAme);
                        recipeTypeView.setText(recipeTYpe);
                        recipeDescription.setText(recipeMethod);
                        String recipeItemsAll = "";
                        for (DataSnapshot child: datas.child("recipeAmount").getChildren()) {
                            recipeItemsAll += (child.getKey() != null)?child.getValue().toString()+" "+child.getKey()+"\n":"";
                        }
                        for (DataSnapshot child: datas.child("recipeG").getChildren()) {
                            recipeItemsAll += (child.getKey() != null)?"\n"+child.getValue().toString()+" grams of "+child.getKey():"";
                        }
                        for (DataSnapshot child: datas.child("recipeML").getChildren()) {
                            recipeItemsAll += (child.getKey() != null)?"\n"+child.getValue().toString()+" ML of "+child.getKey():"";
                        }
                        recipeItems.setText(recipeItemsAll);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        /*
        if (getIntent().hasExtra("category")){
            String category = getIntent().getStringExtra("category");
            String brand = getIntent().getStringExtra("brandId");
            String recipeName = getIntent().getStringExtra("recipeName");
            categotyTv.setText(category);
            brandTv.setText(recipeName);
        }
*/


    }
}