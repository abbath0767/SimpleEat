package com.luxary_team.simpleeat.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.luxary_team.simpleeat.Recipe;
import com.luxary_team.simpleeat.database.RecipeDBShema.*;

import java.util.UUID;

public class RecipeCursorWrapper extends CursorWrapper {
    public RecipeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Recipe getRecipe() {
        String uuidString = getString(getColumnIndex(RecipeTable.Cols.UUID));
        String title = getString(getColumnIndex(RecipeTable.Cols.TITLE));
        int isSolved = getInt(getColumnIndex(RecipeTable.Cols.FAVORITE));
        String recipeType = getString(getColumnIndex(RecipeTable.Cols.TYPE));

        Recipe recipe = new Recipe(UUID.fromString(uuidString));
        recipe.setTitle(title);
        recipe.setFavotite(isSolved != 0);
        recipe.setRecipeType(Recipe.RecipeType.valueOf(recipeType));
        return recipe;
    }
}
