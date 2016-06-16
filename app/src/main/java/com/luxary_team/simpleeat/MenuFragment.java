package com.luxary_team.simpleeat;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.luxary_team.simpleeat.interfaces.SelectItemDrawerCallback;
import com.luxary_team.simpleeat.objects.Recipe;
import com.luxary_team.simpleeat.objects.RecipeLab;
import com.luxary_team.simpleeat.objects.RecipeType;
import com.luxary_team.simpleeat.ui.KitchenStep1Fragment;

import java.util.ArrayList;

public class MenuFragment extends ListFragment {

    private ArrayList<Recipe> mRecipes;
    private ArrayList<RecipeType> mUsedRecipeTypes = new ArrayList<>();
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
                Recipe recipe = new Recipe();
                Fragment fragment = KitchenStep1Fragment.newInstance(recipe);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment).commit();
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

        ArrayAdapter<RecipeType> adapter = new ArrayAdapter<RecipeType>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                mUsedRecipeTypes);

        setListAdapter(adapter);

        setHasOptionsMenu(true);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        RecipeType recipeType = (RecipeType) getListAdapter().getItem(position);

        RecipeListFragment recipeListFragment = (RecipeListFragment) RecipeListFragment.newInstance(recipeType);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, recipeListFragment)
                .addToBackStack(null)
                .commit();

    }

    //todo make logic
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(MainActivity.TAG, "Submit: " + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(MainActivity.TAG, "Change: " + newText);
                return false;
            }
        });

        super.onPrepareOptionsMenu(menu);
    }

    private void calculateRecipeType() {
        for (Recipe recipe: mRecipes) {
            if (recipe.isFavorite()) {
                mUsedRecipeTypes.add(RecipeType.FAVORITE);
                break;
            }
        }

        for (Recipe recipe: mRecipes)
            if (!mUsedRecipeTypes.contains(recipe.getRecipeType()))
                mUsedRecipeTypes.add(recipe.getRecipeType());

    }
}
