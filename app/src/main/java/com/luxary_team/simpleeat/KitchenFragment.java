package com.luxary_team.simpleeat;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;

public class KitchenFragment extends Fragment{

    private EditText mTitleEditText;
    private Spinner mTypeSpinner;
    private Button mAddRecipeButton;

    private RecipeLab lab;
    private Recipe.RecipeType mRecipeType;
    private Recipe.RecipeType[] recipeTypesWithoutFavorite;
    private Recipe mRecipe;
//    private String[] recipeTypes;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = new Recipe();
        lab = RecipeLab.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kitchen_fragment, container, false);

        //remove last element
        recipeTypesWithoutFavorite = Recipe.RecipeType.values();
        recipeTypesWithoutFavorite = Arrays.copyOf(recipeTypesWithoutFavorite, recipeTypesWithoutFavorite.length - 1);

        mTitleEditText = (EditText) view.findViewById(R.id.new_recipe_title_edit_text);
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecipe.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mTypeSpinner = (Spinner) view.findViewById(R.id.new_resipe_type_spinner);
        ArrayAdapter<Recipe.RecipeType> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_expandable_list_item_1, recipeTypesWithoutFavorite);
        mTypeSpinner.setAdapter(adapter);
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int position, long selectedId) {

                Recipe.RecipeType recipe = (Recipe.RecipeType) parent.getAdapter().getItem(position);

                mRecipeType = recipe;
                mRecipe.setRecipeType(mRecipeType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mAddRecipeButton = (Button) view.findViewById(R.id.kitchen_add_recipe_button);
        mAddRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.TAG, "add new recipe ");
                Log.d(MainActivity.TAG, "         title: " + mRecipe.getTitle());
                Log.d(MainActivity.TAG, "          type: " + mRecipe.getRecipeType());
                Log.d(MainActivity.TAG, "          type: " + mRecipe.getRecipeType().toString());

                lab.addRecipe(mRecipe);

                Log.d(MainActivity.TAG, "recipes count = " + lab.getRecipes().size());
            }
        });

        return view;
    }
}
