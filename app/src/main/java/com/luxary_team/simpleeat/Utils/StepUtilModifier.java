package com.luxary_team.simpleeat.Utils;

import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.luxary_team.simpleeat.objects.Recipe;
import com.luxary_team.simpleeat.objects.RecipeChild;

import java.util.ArrayList;

public class StepUtilModifier {
    public static  <T extends RecipeChild> void updateArrayOfChilds(T element, ArrayList<T> array, Recipe recipe) {
        if (!array.contains(element)) {
            element.setParentRecipeUUID(recipe.getId().toString());
            array.add(element);
        }
    }

    public static  void setClickableRemoveImageButton(LinearLayout linearLayout, ImageButton button) {
        if (linearLayout.getChildCount() == 1) {
            button.setClickable(false);
            button.setEnabled(false);
        } else {
            button.setClickable(true);
            button.setEnabled(true);
        }
    }
}
