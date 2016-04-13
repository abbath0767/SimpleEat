package com.luxary_team.simpleeat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.luxary_team.simpleeat.database.RecipeDBShema.*;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
