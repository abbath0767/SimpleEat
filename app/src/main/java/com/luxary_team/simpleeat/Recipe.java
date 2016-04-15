package com.luxary_team.simpleeat;

import java.util.UUID;

public class Recipe {
    private UUID mId;
    private String mTitle;
    private RecipeType mType;
    private boolean favorite;

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

    public enum RecipeType{
        //todo vaforite must be last element!!! always
        SOUP ("Супы"),
        TESTING ("TestingRecipeType"),
        SIMPLE ("simple recipes"),
        FAVORITE ("Избранное");

        private final String name;

        RecipeType(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }

    }
}
