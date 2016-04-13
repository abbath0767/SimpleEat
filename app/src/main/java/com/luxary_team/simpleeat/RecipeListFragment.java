package com.luxary_team.simpleeat;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RecipeListFragment extends ListFragment {
    //todo add lifecycle
    public static final String EXTRA_RECIPE_TYPE = "recipeType";

    private Recipe.RecipeType mRecipeType;
    private ArrayList<Recipe> mRecipes;

    public static ListFragment newInstance(Recipe.RecipeType recipeType) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_RECIPE_TYPE, recipeType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list_fragment, container, false);

        ListView recipeListView = (ListView) view.findViewById(android.R.id.list);
        recipeListView.setEmptyView(view.findViewById(android.R.id.empty));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipes = new ArrayList<>();
        mRecipeType = (Recipe.RecipeType) getArguments().getSerializable(EXTRA_RECIPE_TYPE);
        calculateRecipeArray();

        ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                mRecipes);
        setListAdapter(adapter);

        setRetainInstance(true);
    }



    public void calculateRecipeArray() {
        ArrayList<Recipe> allRecipes = RecipeLab.get(getActivity()).getRecipes();

        if (mRecipeType.equals(Recipe.RecipeType.FAVORITE)) {
            for (Recipe recipe : allRecipes) {
                if (recipe.isFavorite())
                    mRecipes.add(recipe);
            }
        } else {
            for (Recipe recipe : allRecipes) {
                if (recipe.getRecipeType().equals(mRecipeType)) {
                    mRecipes.add(recipe);
                }
            }
        }
    }
}
