package com.luxary_team.simpleeat.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luxary_team.simpleeat.database.RecipeDBHelper;
import com.luxary_team.simpleeat.database.RecipeStepCursorWrapper;
import com.luxary_team.simpleeat.database.RecipeStepDBShema.RecipeStepTable;

import java.util.ArrayList;

public class RecipeStepLab {
    private static RecipeStepLab mRecipeStepLab;
    private Context mContext;
    private ArrayList<RecipeStep> mRecipeSteps = new ArrayList<>();
    private SQLiteDatabase mDatabase;

    private RecipeStepLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RecipeDBHelper(context).getWritableDatabase();
    }

    public static RecipeStepLab get(Context context) {
        if (mRecipeStepLab == null)
            mRecipeStepLab = new RecipeStepLab(context);
        return mRecipeStepLab;
    }

    public void setAndSaveRecipeSteps(ArrayList<RecipeStep> recipeSteps) {
        mRecipeSteps = recipeSteps;
        for (RecipeStep step: mRecipeSteps)
            addRecipeStep(step);
    }

    public ArrayList<RecipeStep> getRecipeSteps(String parentUuid) {
        updateRecipeSteps(parentUuid);
        return mRecipeSteps;
    }

    public void updateRecipeSteps(String parentUuid) {
        RecipeStepCursorWrapper cursor = queryRecipeSteps(RecipeStepTable.Cols.PARENT_UUID + " = ?",
                new String[] {parentUuid});

        mRecipeSteps.clear();

        try {
            if (cursor.getCount() == 0)
                return;;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                mRecipeSteps.add(cursor.getRecipeStep());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    public void addRecipeStep(RecipeStep step) {
        ContentValues content = getContentValues(step);
        mDatabase.insert(RecipeStepTable.NAME, null, content);
    }

    private RecipeStepCursorWrapper queryRecipeSteps(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(RecipeStepTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new RecipeStepCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(RecipeStep recipeStep) {
        ContentValues content = new ContentValues();
        content.put(RecipeStepTable.Cols.NAME, recipeStep.getName());
        content.put(RecipeStepTable.Cols.NUM, recipeStep.getNum());
        content.put(RecipeStepTable.Cols.PARENT_UUID, recipeStep.getParentRecipeUUID());

        return content;
    }

}
