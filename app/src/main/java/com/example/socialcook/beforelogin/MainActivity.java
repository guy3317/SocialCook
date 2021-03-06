package com.example.socialcook.beforelogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialcook.R;
import com.example.socialcook.recievenotification.ReceiveNotificationActivity;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "auth";
    private static int SPLASH_TIME_OUT = 4000;
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    static final int REQUEST_CODE = 123;
    private TextView emailText;
    private TextView passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            if (getIntent().hasExtra("recipeName")){
                Intent intent = new Intent(this, ReceiveNotificationActivity.class);
                Bundle bndl = getIntent().getExtras();
                intent.putExtra("category",getIntent().getStringExtra("category"));
                intent.putExtra("brandId",getIntent().getStringExtra("brandId"));
                intent.putExtra("recipeName",getIntent().getStringExtra("recipeName"));
                intent.putExtras(bndl);
                startActivity(intent);
                finish();
            }
            else if(FireBase.getAuth().getCurrentUser() != null) {
                loadOnline();
                finish();
            }
            else {
                FirebaseAuth mAuth = FireBase.getAuth();
                // Create a new Fragment to be placed in the activity layout
                Fragment firstFragment = new LoginFragment();
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                firstFragment.setArguments(getIntent().getExtras());
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if((grantResults.length>0) && grantResults[0] + grantResults[1] + grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void loadLoginFrag() {
        // Create fragment and give it an argument specifying the article it should show
        Fragment newFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();
    }


    public void loadRegister() {
        // Create fragment and give it an argument specifying the article it should show
        Fragment registerFrag = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, registerFrag);
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();
    }
    public void loadOnline() {
        Intent next = new Intent(this , MainPage.class);
        startActivity(next);
        finish();
    }
}