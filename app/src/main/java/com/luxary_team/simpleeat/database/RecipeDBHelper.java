package com.luxary_team.simpleeat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.luxary_team.simpleeat.MainActivity;
import com.luxary_team.simpleeat.database.RecipeDBShema.RecipeTable;
import com.luxary_team.simpleeat.database.RecipeElementDBShame.RecipeElementTable;
import com.luxary_team.simpleeat.database.RecipeStepDBShema.RecipeStepTable;

public class RecipeDBHelper extends SQLiteOpenHelper{
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "simpleeat.db";

    public RecipeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RecipeTable.NAME + "(" +
                    "_id integer primary key autoincrement, " +
                    RecipeTable.Cols.UUID + ", " +
                    RecipeTable.Cols.TITLE + ", " +
                    RecipeTable.Cols.FAVORITE + ", " +
                    RecipeTable.Cols.TYPE + ")");
        Log.d(MainActivity.TAG, "recipes db created succesfully");

        db.execSQL("create table " + RecipeElementTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                RecipeElementTable.Cols.NAME + ", " +
                RecipeElementTable.Cols.COUNT + ", " +
                RecipeElementTable.Cols.PARENT_UUID + ")");
        Log.d(MainActivity.TAG, "recipe_elements db created succesfully");

        db.execSQL("create table " + RecipeStepTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                RecipeStepTable.Cols.NAME + ", " +
                RecipeStepTable.Cols.NUM + ", " +
                RecipeStepTable.Cols.PARENT_UUID + ")");
        Log.d(MainActivity.TAG, "recipe_steps db created succesfully");

        Log.d(MainActivity.TAG, "all db created succesfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
