package com.luxary_team.simpleeat.database;

public class RecipeDBShema {
    public static final class RecipeTable {
        public static final String NAME = "recipes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String FAVORITE = "favorite";
            public static final String TYPE = "type";

        }
    }
}
