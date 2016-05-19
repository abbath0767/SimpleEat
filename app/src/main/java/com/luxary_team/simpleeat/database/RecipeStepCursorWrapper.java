package com.luxary_team.simpleeat.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.luxary_team.simpleeat.objects.RecipeStep;
import com.luxary_team.simpleeat.database.RecipeStepDBShema.RecipeStepTable;

public class RecipeStepCursorWrapper extends CursorWrapper {
    public RecipeStepCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public RecipeStep getRecipeStep() {
        String name = getString(getColumnIndex(RecipeStepTable.Cols.NAME));
        int stepNum = getInt(getColumnIndex(RecipeStepTable.Cols.NUM));
        String uuidString = getString(getColumnIndex(RecipeStepTable.Cols.PARENT_UUID));

        RecipeStep recipeStep = new RecipeStep();
        recipeStep.setName(name);
        recipeStep.setNum(stepNum);
        recipeStep.setParentRecipeUUID(uuidString);

        return recipeStep;
    }
}
