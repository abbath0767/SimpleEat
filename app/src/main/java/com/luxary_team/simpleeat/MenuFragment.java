package com.luxary_team.simpleeat;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.luxary_team.simpleeat.interfaces.SelectItemDrawerCallback;
import com.luxary_team.simpleeat.objects.Recipe;
import com.luxary_team.simpleeat.objects.RecipeLab;

import java.util.ArrayList;

public class MenuFragment extends ListFragment {

    private ArrayList<Recipe> mRecipes;
    private ArrayList<Recipe.RecipeType> mUsedRecipeTypes = new ArrayList<>();
    private Button mButton;
    private SelectItemDrawerCallback mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);

        ListView recipeListView = (ListView) view.findViewById(android.R.id.list);
        recipeListView.setEmptyView(view.findViewById(android.R.id.empty));

        mButton = (Button) view.findViewById(R.id.button_add_recipe);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.selectItemInDrawer(1);
                //todo tested
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new KitchenFragment()).commit();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipes = RecipeLab.get(getActivity()).getRecipes();
        calculateRecipeType();

        mCallback = (SelectItemDrawerCallback) getActivity();

        ArrayAdapter<Recipe.RecipeType> adapter = new ArrayAdapter<Recipe.RecipeType>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                mUsedRecipeTypes);

        setListAdapter(adapter);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.menu);

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
                break;
            }
        }

        for (Recipe recipe: mRecipes)
            if (!mUsedRecipeTypes.contains(recipe.getRecipeType()))
                mUsedRecipeTypes.add(recipe.getRecipeType());

    }
}
