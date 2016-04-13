package com.luxary_team.simpleeat;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;

public class KitchenFragment extends Fragment{

    private EditText mTitleEditText;
    private Spinner mTypeSpinner;
    private Recipe.RecipeType mRecipeType;
    private Recipe.RecipeType[] recipeTypesWithoutFavorite;
//    private String[] recipeTypes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kitchen_fragment, container, false);

//        recipeTypes = getResources().getStringArray(R.array.recipe_type_array);
        //remove last element
        recipeTypesWithoutFavorite = Recipe.RecipeType.values();
        recipeTypesWithoutFavorite = Arrays.copyOf(recipeTypesWithoutFavorite, recipeTypesWithoutFavorite.length - 1);

        Log.d(MainActivity.TAG, "array = " + Arrays.toString(recipeTypesWithoutFavorite));

        mTitleEditText = (EditText) view.findViewById(R.id.new_recipe_title_edit_text);

        mTypeSpinner = (Spinner) view.findViewById(R.id.new_resipe_type_spinner);
        ArrayAdapter<Recipe.RecipeType> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_expandable_list_item_1, recipeTypesWithoutFavorite);
        mTypeSpinner.setAdapter(adapter);
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int position, long selectedId) {

                Recipe.RecipeType recipe = (Recipe.RecipeType) parent.getAdapter().getItem(position);

                Log.d(MainActivity.TAG, "test type = " + recipe);

                mRecipeType = recipe;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
}
