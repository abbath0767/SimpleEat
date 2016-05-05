package com.luxary_team.simpleeat;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class KitchenFragment extends Fragment{

    private EditText mTitleEditText;
    private Spinner mTypeSpinner;
    private Button mAddRecipeButton;
    private LinearLayout mLinearLayoutContainerElements;
    private ImageButton mImageButtonAddElement;

    private RecipeLab mRecipeLab;
    private RecipeElementLab mRecipeElementLab;
    private Recipe.RecipeType mRecipeType;
    private Recipe.RecipeType[] recipeTypesWithoutFavorite;
    private Recipe mRecipe;
    private ArrayList<RecipeElement> mRecipeElements;
//    private String[] recipeTypes;

    public static Fragment newInstance() {
        KitchenFragment fragment = new KitchenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = new Recipe();
        Log.d(MainActivity.TAG, "new recipe created, uuid = " + mRecipe.getId().toString());
        mRecipeLab = RecipeLab.get(getActivity());
        mRecipeElementLab = RecipeElementLab.get(getActivity());
        mRecipeElements = new ArrayList<>();
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
                mRecipeLab.addRecipe(mRecipe);

                mRecipeElementLab.setAndSaveRecipeElements(mRecipeElements);
                Toast.makeText(getActivity(), "Новый рецепт создан успешно!", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new MenuFragment()).commit();
            }
        });

        mLinearLayoutContainerElements = (LinearLayout) view.findViewById(R.id.list_view_element_container);

        mImageButtonAddElement = (ImageButton) view.findViewById(R.id.kitchen_button_add_element);
        mImageButtonAddElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RecipeElement anotherRecipeElement = new RecipeElement();

                LinearLayout anotherLL = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                        R.layout.list_item_recipe_element_new, null);
                anotherLL.setId(View.generateViewId());

                mLinearLayoutContainerElements.addView(anotherLL, mLinearLayoutContainerElements.getChildCount() - 1);

                EditText mEditTextName = (EditText) anotherLL.findViewById(R.id.recipe_element_name_editText);
                mEditTextName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        anotherRecipeElement.setName(s.toString());
                        updateRecipeElements(anotherRecipeElement);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                EditText mEditTextCount = (EditText) anotherLL.findViewById(R.id.recipe_element_count_editText);
                mEditTextCount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        anotherRecipeElement.setCount(s.toString());
                        updateRecipeElements(anotherRecipeElement);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }
        });


        return view;
    }

    private void updateRecipeElements(RecipeElement element) {
        if (!mRecipeElements.contains(element)) {
            element.setParentRecipeUUID(mRecipe.getId().toString());
            mRecipeElements.add(element);
        }
    }
}
