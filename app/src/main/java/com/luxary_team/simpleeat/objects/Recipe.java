package com.luxary_team.simpleeat.objects;

import java.io.Serializable;
import java.util.UUID;

public class Recipe implements Serializable{
    private UUID mId;
    private String mTitle;
    private RecipeType mType;
    private boolean favorite;
    //new parameters
    private int portionCount;
    private String recipeTime;

    public String getPhotoFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public Recipe() {
        this.mId = UUID.randomUUID();
    }

    public Recipe(UUID uuid) {this.mId = uuid;}

    public Recipe(String title, RecipeType type) {
        mId = UUID.randomUUID();
        this.mTitle = title;
        this.mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String tittle) {
        this.mTitle = tittle;
    }

    public UUID getId() {
        return mId;
    }

    public void setFavotite(boolean favorite) {this.favorite = favorite;}

    public boolean isFavorite() {return favorite;}

    public void setRecipeType(RecipeType type) {this.mType = type;}

    public RecipeType getRecipeType() {return mType;}

    public String getRecipeTime() {
        return recipeTime;
    }

    public void setRecipeTime(String time) {
        this.recipeTime = time;
    }

    public int getPortionCount() {
        return portionCount;
    }

    public void setPortionCount(int portionCount) {
        this.portionCount = portionCount;
    }

    @Override
    public String toString() {
        return mTitle;
    }

}
