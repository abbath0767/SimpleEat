package com.luxary_team.simpleeat.database;

public class RecipeStepDBShema {
    public static final class RecipeStepTable {
        public static final String NAME = "recipesteps";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String NUM = "number";
            public static final String PARENT_UUID = "parent_uuid";
        }
    }
}
