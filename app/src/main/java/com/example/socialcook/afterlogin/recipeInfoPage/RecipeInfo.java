package com.example.socialcook.afterlogin.recipeInfoPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.recipeListPage.MainPage;
import com.example.socialcook.afterlogin.recipeListPage.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecipeInfo extends Fragment {
    private static final String TAG = "<<< TESTING >>>";
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_info, container, false);
        final MainPage mainPage = (MainPage)getActivity();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Bundle extras = this.getArguments();
            final Recipe currentRecipe= (Recipe) extras.getSerializable("recipe");
            Log.d(TAG, "1: "+currentRecipe.getRecipeAmount().keySet().size());
            TextView recipeInfoView = view.findViewById(R.id.recipeInfo);
            //TextView recipeIngrediantsView = view.findViewById(R.id.recipeIngrediants);
            recipeInfoView.setText("Recipe name : "+currentRecipe.getRecipeName() + "\n\nRecipe type : " + currentRecipe.getRecipeType() + "\n\nRequirements\n"+currentRecipe.convertRecipeAmountIteration()+""+currentRecipe.convertRecipeMLIteration()+""+currentRecipe.convertRecipeGIteration()+"\nDescription\n"+currentRecipe.getRecipeDescription());
            //System.out.println(currentRecipe.convertRecipeMLIteration());
            //recipeIngrediantsView.setText(currentRecipe.convertRecipeMLIteration());
            Button nextButton = view.findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainPage.loadUsersPage(currentRecipe);
                }
            });
        }
        // Inflate the layout for this fragment
        return view;
    }
}