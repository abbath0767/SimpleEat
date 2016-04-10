package com.luxary_team.simpleeat;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

public class RecipeLab {

    private static RecipeLab sRecipeLab;
    private Context mContext;

    ArrayList<Recipe> mRecipes;

    //todo testing
    public RecipeLab() {
        mRecipes = new ArrayList<>();
        for (int i = 0; i < 5 ; i++)
            mRecipes.add(new Recipe("new recipe %" + i, Recipe.RecipeType.SOUP));
        mRecipes.add(new Recipe("test recipe in testing", Recipe.RecipeType.TESTING));
        mRecipes.add(new Recipe("test recipe in testing2", Recipe.RecipeType.TESTING));
        mRecipes.add(new Recipe("test recipe in testing3", Recipe.RecipeType.SIMPLE));
    }

    private RecipeLab(Context appContext) {
        mContext = appContext;
        mRecipes = new ArrayList<>();
        for (int i = 0; i < 5 ; i++)
            mRecipes.add(new Recipe("new recipe %" + i, Recipe.RecipeType.SOUP));
        mRecipes.add(new Recipe("test recipe in testing", Recipe.RecipeType.TESTING));
        mRecipes.add(new Recipe("test recipe in testing2", Recipe.RecipeType.TESTING));
        mRecipes.add(new Recipe("test recipe in testing3", Recipe.RecipeType.SIMPLE));

    }

    public static RecipeLab get(Context c){
        if (sRecipeLab == null) {
            sRecipeLab = new RecipeLab(c.getApplicationContext());
        }
        return sRecipeLab;
    }

    public void addRecipe(Recipe recipe) {
        mRecipes.add(recipe);
    }

    public Recipe getRecipe(UUID id) {
        for (Recipe recipe: mRecipes)
            if (recipe.getId().equals(id))
                return recipe;

        return null;
    }

    public ArrayList<Recipe> getRecipes() {
        return mRecipes;
    }


}
