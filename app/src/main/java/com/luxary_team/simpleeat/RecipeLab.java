package com.luxary_team.simpleeat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luxary_team.simpleeat.database.RecipeCursorWrapper;
import com.luxary_team.simpleeat.database.RecipeDBHelper;
import com.luxary_team.simpleeat.database.RecipeDBShema.*;

import java.util.ArrayList;
import java.util.UUID;

public class RecipeLab {

    private static RecipeLab sRecipeLab;
    private Context mContext;
    private SQLiteDatabase mDataBase;

    private RecipeLab(Context appContext) {
        mContext = appContext.getApplicationContext();
        mDataBase = new RecipeDBHelper(mContext).getWritableDatabase();
    }

    public static RecipeLab get(Context c){
        if (sRecipeLab == null) {
            sRecipeLab = new RecipeLab(c.getApplicationContext());
        }
        return sRecipeLab;
    }

    public void addRecipe(Recipe recipe) {
        ContentValues content = getContentValues(recipe);

        mDataBase.insert(RecipeTable.NAME, null, content);
    }

    public void deleteRecipe(Recipe recipe) {
        mDataBase.delete(RecipeTable.NAME, RecipeTable.Cols.UUID + " = ?", new String[]{recipe.getId().toString()});
    }

    public Recipe getRecipe(UUID id) {
        RecipeCursorWrapper cursor = queryRecipes(RecipeTable.NAME + " = ?", new String[]{id.toString()});

        try {
            if(cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getRecipe();
        } finally {
            cursor.close();
        }
    }

    public void updateRecipe(Recipe recipe) {
        String uuidString =recipe.getId().toString();
        ContentValues content = getContentValues(recipe);

        mDataBase.update(RecipeTable.NAME, content, RecipeTable.Cols.UUID + " = ?", new String[]{uuidString});

    }

    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> mRecipes = new ArrayList<>();
        RecipeCursorWrapper cursor = queryRecipes(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                mRecipes.add(cursor.getRecipe());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mRecipes;
    }

    private RecipeCursorWrapper queryRecipes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDataBase.query(RecipeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new RecipeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Recipe recipe) {
        ContentValues content = new ContentValues();
        content.put(RecipeTable.Cols.UUID, recipe.getId().toString());
        content.put(RecipeTable.Cols.TITLE, recipe.getTitle());
        content.put(RecipeTable.Cols.FAVORITE, recipe.isFavorite() ? 1 : 0);
        content.put(RecipeTable.Cols.TYPE, recipe.getRecipeType().toString());

        return content;
    }


}
