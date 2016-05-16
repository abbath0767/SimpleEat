package com.luxary_team.simpleeat.objects;

public class RecipeElement extends RecipeChild{
    private String mName;
    private String mCount;

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
