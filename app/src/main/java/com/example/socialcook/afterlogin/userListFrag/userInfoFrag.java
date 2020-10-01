package com.example.socialcook.afterlogin.userListFrag;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.socialcook.R;
import com.example.socialcook.classes.User;
import com.example.socialcook.firebase.FireBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class userInfoFrag extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        Bundle extras = this.getArguments();
        final User currentUser = (User) extras.getSerializable("user");
        TextView userName = view.findViewById(R.id.nameText);
        TextView userAddress = view.findViewById(R.id.addressText);
        TextView userAge = view.findViewById(R.id.ageText);
        TextView userDescription = view.findViewById(R.id.userDescription);
        final ImageView userPhoto = view.findViewById(R.id.userPhoto);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar4);
        userName.setText(currentUser.getName());
        userAddress.setText(currentUser.getAddress());
        userAge.setText(currentUser.getBirthday()); // NEED TO FORMAT INTO AGE
        userDescription.setText(currentUser.getDescription());
        String imagePath = currentUser.getImagePath();
        if (imagePath != null)
        {
            FireBase.storageRef.child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    // Load the image using Glide
                    Glide
                            .with(getContext())
                            .load(uri)
                            .centerCrop()
                            .placeholder(progressBar.getProgressDrawable())
                            .into(userPhoto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        return view;
    }
}