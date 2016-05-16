package com.luxary_team.simpleeat.objects;

public class RecipeStep extends RecipeChild {
    private String mDiscription;
    private int mNum;

    public String getDiscription() {
        return mDiscription;
    }

    public void setDiscription(String discription) {
        mDiscription = discription;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    @Override
    public String toString() {
        return "RecipeStep{" +
                "mDiscription='" + mDiscription + '\'' +
                ", mParentRecipeUUID='" + mParentRecipeUUID + '\'' +
                ", mNum=" + mNum +
                '}';
    }
}
