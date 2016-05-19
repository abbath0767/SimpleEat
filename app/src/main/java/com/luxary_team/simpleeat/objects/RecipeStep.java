package com.luxary_team.simpleeat.objects;

public class RecipeStep extends RecipeChild {
    private int mNum;

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    @Override
    public String toString() {
        return "RecipeStep{" +
                "mName='" + mName + '\'' +
                ", mParentRecipeUUID='" + mParentRecipeUUID + '\'' +
                ", mNum=" + mNum +
                '}';
    }
}
