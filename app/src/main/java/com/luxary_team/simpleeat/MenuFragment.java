package com.luxary_team.simpleeat;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MenuFragment extends ListFragment {

    private ArrayList<Recipe> mRecipes;
    private ArrayList<Recipe.RecipeType> mUsedRecipeTypes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);

        ListView recipeListView = (ListView) view.findViewById(android.R.id.list);
        recipeListView.setEmptyView(view.findViewById(android.R.id.empty));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipes = RecipeLab.get(getActivity()).getRecipes();
        calculateRecipeType();

        ArrayAdapter<Recipe.RecipeType> adapter = new ArrayAdapter<Recipe.RecipeType>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                mUsedRecipeTypes);

        setListAdapter(adapter);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Recipe.RecipeType recipeType = (Recipe.RecipeType) getListAdapter().getItem(position);

        Log.d(MainActivity.TAG, "on item click " + position);

        RecipeListFragment recipeListFragment = (RecipeListFragment) RecipeListFragment.newInstance(recipeType);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, recipeListFragment)
                .addToBackStack(null)
                .commit();

    }

    private void calculateRecipeType() {
        for (Recipe recipe: mRecipes) {
            if (recipe.isFavorite()) {
                mUsedRecipeTypes.add(Recipe.RecipeType.FAVORITE);
            }
        }

        for (Recipe recipe: mRecipes)
            if (!mUsedRecipeTypes.contains(recipe.getRecipeType()))
                mUsedRecipeTypes.add(recipe.getRecipeType());

    }
}
