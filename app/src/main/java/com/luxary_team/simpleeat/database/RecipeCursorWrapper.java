package com.luxary_team.simpleeat.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.luxary_team.simpleeat.MainActivity;
import com.luxary_team.simpleeat.Recipe;
import com.luxary_team.simpleeat.database.RecipeDBShema.RecipeTable;

import java.util.UUID;

public class RecipeCursorWrapper extends CursorWrapper {
    public RecipeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    //todo need refactor!
    public Recipe getRecipe() {
        String uuidString = getString(getColumnIndex(RecipeTable.Cols.UUID));
        String title = getString(getColumnIndex(RecipeTable.Cols.TITLE));
        int isSolved = getInt(getColumnIndex(RecipeTable.Cols.FAVORITE));
        String recipeType = getString(getColumnIndex(RecipeTable.Cols.TYPE));

        Log.d(MainActivity.TAG, "wrapper recyзe type str = " + recipeType);
        Recipe.RecipeType mode = null;
        for (Recipe.RecipeType r: Recipe.RecipeType.values()) {
            if (recipeType.equals(r.toString()))
                mode = r;
        }

        Recipe recipe = new Recipe(UUID.fromString(uuidString));
        recipe.setTitle(title);
        recipe.setFavotite(isSolved != 0);
        Recipe.RecipeType mode1 = Recipe.RecipeType.SOUP;
        Log.d(MainActivity.TAG, "soup?  " + (mode == mode1));
        Log.d(MainActivity.TAG, "soup?  " + (mode.toString()));
        Log.d(MainActivity.TAG, "soup?  " + (mode1.toString()));
        recipe.setRecipeType(mode);
        return recipe;
    }
}
