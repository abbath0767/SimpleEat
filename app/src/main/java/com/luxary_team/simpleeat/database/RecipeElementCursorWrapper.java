package com.luxary_team.simpleeat.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.luxary_team.simpleeat.objects.RecipeElement;
import com.luxary_team.simpleeat.database.RecipeElementDBShame.RecipeElementTable;

public class RecipeElementCursorWrapper extends CursorWrapper{
    public RecipeElementCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public RecipeElement getRecipeElement() {
        String name = getString(getColumnIndex(RecipeElementTable.Cols.NAME));
        String count = getString(getColumnIndex(RecipeElementTable.Cols.COUNT));
        String uuidString = getString(getColumnIndex(RecipeElementTable.Cols.PARENT_UUID));

        RecipeElement element = new RecipeElement();
        element.setName(name);
        element.setCount(count);
        element.setParentRecipeUUID(uuidString);

        return element;
    }
}
