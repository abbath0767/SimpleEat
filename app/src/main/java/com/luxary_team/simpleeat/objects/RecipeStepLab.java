package com.luxary_team.simpleeat.objects;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.luxary_team.simpleeat.database.RecipeDBHelper;

import java.util.ArrayList;

public class RecipeStepLab {
    private static RecipeStepLab mRecipeStepLab;
    private Context mContext;
    private ArrayList<RecipeStep> mRecipeSteps = new ArrayList<>();
    private SQLiteDatabase mDatabase;

    private RecipeStepLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RecipeDBHelper(context).getWritableDatabase();

        //todo go next make controller
    }
}
