package com.luxary_team.simpleeat.objects;

public abstract class RecipeChild {
    public String mParentRecipeUUID;

    public String getParentRecipeUUID() {
        return mParentRecipeUUID;
    }

    public void setParentRecipeUUID(String parentRecipeUUID) {
        mParentRecipeUUID = parentRecipeUUID;
    }
}
