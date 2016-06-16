package com.luxary_team.simpleeat.objects;

public enum RecipeType {
    //vaforite must be last element!!! always
    //sync with string res
    SOUP("Супы"),
    SECOND("Второе"),
    SIMPLE("Простой рецепт"),
    DRINK("Напитки"),
    SALAT("Салат"),
    FAVORITE("Избранное");

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
