package com.luxary_team.simpleeat;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.UUID;

public class RecipeFragment extends Fragment {
    public static final String EXTRA_RECIPE_ID = "com.rmr.ngusarov.simpleeat.recipe_id";
    public static final int DELETE_BUTTON_ID = 1;

    private TextView mRecipeTitle;
    private TextView mRecipeTextType;
    private CheckBox mFavorCheckBox;

    private Recipe mRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID recipeUuid = (UUID) getArguments().getSerializable(EXTRA_RECIPE_ID);
        mRecipe = RecipeLab.get(getActivity()).getRecipe(recipeUuid);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_fragment, container, false);

        mRecipeTitle = (TextView) rootView.findViewById(R.id.recipe_fragment_title_textView);
        mRecipeTitle.setText(mRecipe.getTitle());

        mRecipeTextType = (TextView) rootView.findViewById(R.id.recipe_fragment_type_textView);
        mRecipeTextType.setText(mRecipe.getRecipeType().toString());

        mFavorCheckBox = (CheckBox) rootView.findViewById(R.id.recipe_fragment_favorite_checkBox);
        mFavorCheckBox.setChecked(mRecipe.isFavorite());
        mFavorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRecipe.setFavotite(isChecked);
            }
        });

        return rootView;
    }

    public static RecipeFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_RECIPE_ID, uuid);
        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(MainActivity.TAG, "RecipeFragment onPause");
        RecipeLab.get(getActivity()).updateRecipe(mRecipe);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, DELETE_BUTTON_ID, 0, R.string.delete_button)
                .setIcon(R.drawable.ic_delete_recipe_button)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case DELETE_BUTTON_ID:
                RecipeLab.get(getActivity()).deleteRecipe(mRecipe);
                getFragmentManager().popBackStackImmediate();
                return true;
            case android.R.id.home:
                getFragmentManager().popBackStackImmediate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
