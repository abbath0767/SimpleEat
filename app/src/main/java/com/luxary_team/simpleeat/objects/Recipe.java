package com.luxary_team.simpleeat.objects;

import java.io.Serializable;
import java.util.UUID;

public class Recipe implements Serializable{
    private UUID mId;
    private String mTitle;
    private RecipeType mType;
    private boolean favorite;

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

    @Override
    public String toString() {
        return mTitle;
    }

}
