package com.luxary_team.simpleeat;

public class RecipeElement {
    private String mName;
    private String mCount;
    private String mParentRecipeUUID;

    public void setParentRecipeUUID(String uuidString) {
        mParentRecipeUUID = uuidString;
    }

    public String getParentRecipeUUID() {
        return mParentRecipeUUID;
    }

    public String getCount() {
        return mCount;
    }

    public void setCount(String count) {
        mCount = count;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return "RecipeElement{" +
                "mName='" + mName + '\'' +
                ", mCount='" + mCount + '\'' +
                ", parent UUID=" + mParentRecipeUUID + '\'' +
                '}';
    }
}
