package com.luxary_team.simpleeat.objects;

public class RecipeElement extends RecipeChild{
    private String mCount;

    public String getCount() {
        return mCount;
    }

    public void setCount(String count) {
        mCount = count;
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
