package com.luxary_team.simpleeat.objects;

public abstract class RecipeChild {
    String mParentRecipeUUID;
    String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getParentRecipeUUID() {
        return mParentRecipeUUID;
    }

    public void setParentRecipeUUID(String parentRecipeUUID) {
        mParentRecipeUUID = parentRecipeUUID;
    }
}
