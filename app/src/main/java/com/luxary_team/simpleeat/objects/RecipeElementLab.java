package com.luxary_team.simpleeat.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luxary_team.simpleeat.database.RecipeDBHelper;
import com.luxary_team.simpleeat.database.RecipeElementCursorWrapper;
import com.luxary_team.simpleeat.database.RecipeElementDBShame.RecipeElementTable;

import java.util.ArrayList;

public class RecipeElementLab {
    private static RecipeElementLab sRecipeElementLab;
    private Context mContext;
    private ArrayList<RecipeElement> mRecipeElements = new ArrayList<>();
    private SQLiteDatabase mDataBase;

    private RecipeElementLab(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new RecipeDBHelper(mContext).getWritableDatabase();
    }

    public static RecipeElementLab get(Context context) {
        if (sRecipeElementLab == null )
            sRecipeElementLab = new RecipeElementLab(context.getApplicationContext());
        return sRecipeElementLab;
    }

    public void setAndSaveRecipeElements(ArrayList<RecipeElement> elements) {
        mRecipeElements = elements;
        for (RecipeElement element: mRecipeElements)
            addRecipeElement(element);
    }

    public ArrayList<RecipeElement> getRecipeElements(String parentUuid) {
        updateRecipeElements(parentUuid);
        return mRecipeElements;
    }

    public void updateRecipeElements(String parentUuid) {
        RecipeElementCursorWrapper cursor = queryRecipeElements(RecipeElementTable.Cols.PARENT_UUID + " = ?",
                new String[]{parentUuid});

        mRecipeElements.clear();

        try {
            if (cursor.getCount() == 0)
                return;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                mRecipeElements.add(cursor.getRecipeElement());
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }
    }

    public void addRecipeElement(RecipeElement element) {
        ContentValues content = getContentValues(element);

        mDataBase.insert(RecipeElementTable.NAME, null, content);
    }

    public RecipeElement getRecipeElement(String parentUuid) {
        //todo
        return null;
    }

    private RecipeElementCursorWrapper queryRecipeElements(String whereClause, String[] whereArgs) {
        Cursor cursor = mDataBase.query(RecipeElementTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new RecipeElementCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(RecipeElement element) {
        ContentValues content = new ContentValues();
        content.put(RecipeElementTable.Cols.NAME, element.getName());
        content.put(RecipeElementTable.Cols.COUNT, element.getCount());
        content.put(RecipeElementTable.Cols.PARENT_UUID, element.getParentRecipeUUID());

        return content;
    }
}
