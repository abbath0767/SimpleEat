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
import android.widget.TextView;
import android.widget.Toast;

import com.luxary_team.simpleeat.interfaces.SelectItemDrawerCallback;
import com.luxary_team.simpleeat.objects.Recipe;
import com.luxary_team.simpleeat.objects.RecipeChild;
import com.luxary_team.simpleeat.objects.RecipeElement;
import com.luxary_team.simpleeat.objects.RecipeElementLab;
import com.luxary_team.simpleeat.objects.RecipeLab;
import com.luxary_team.simpleeat.objects.RecipeStep;
import com.luxary_team.simpleeat.objects.RecipeStepLab;

import java.util.ArrayList;
import java.util.Arrays;

public class KitchenFragment extends Fragment{

    private EditText mTitleEditText;
    private Spinner mTypeSpinner;
    private Button mAddRecipeButton;
    private LinearLayout mLinearLayoutContainerElements;
    private LinearLayout mLinearLayoutContainerSteps;
    private ImageButton mImageButtonAddElement;
    private ImageButton mImageButtonRemoveElement;
    private ImageButton mImageButtonAddStep;
    private ImageButton mImageButtonRemoveStep;

    private RecipeLab mRecipeLab;
    private RecipeElementLab mRecipeElementLab;
    private RecipeStepLab mRecipeStepLab;
    private Recipe.RecipeType mRecipeType;
    private Recipe.RecipeType[] recipeTypesWithoutFavorite;
    private Recipe mRecipe;
    private ArrayList<RecipeElement> mRecipeElements;
    private ArrayList<RecipeStep> mRecipeSteps;
    private SelectItemDrawerCallback mCallback;

    private int stepCount = 1;
//    private String[] recipeTypes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);
        mRecipe = new Recipe();
        Log.d(MainActivity.TAG, "new recipe created, uuid = " + mRecipe.getId().toString());
        mRecipeLab = RecipeLab.get(getActivity());
        mRecipeElementLab = RecipeElementLab.get(getActivity());
        mRecipeStepLab = RecipeStepLab.get(getActivity());
        mRecipeElements = new ArrayList<>();
        mRecipeSteps = new ArrayList<>();

        mCallback = (SelectItemDrawerCallback) getActivity();
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

                Log.d(MainActivity.TAG, "mRecipeSTeps length = " + mRecipeSteps.size());
                for (RecipeStep step: mRecipeSteps)
                    Log.d(MainActivity.TAG, "step: " + step);

                mRecipeStepLab.setAndSaveRecipeSteps(mRecipeSteps);
                mRecipeElementLab.setAndSaveRecipeElements(mRecipeElements);
                Toast.makeText(getActivity(), "Новый рецепт создан успешно!", Toast.LENGTH_SHORT).show();

                mCallback.selectItemInDrawer(0);

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
                        updateArrayOfChilds(anotherRecipeElement, mRecipeElements);
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
                        updateArrayOfChilds(anotherRecipeElement, mRecipeElements);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                setClickableRemoveImageButton(mLinearLayoutContainerElements, mImageButtonRemoveElement);
            }
        });

        mImageButtonRemoveElement = (ImageButton) view.findViewById(R.id.kitchen_button_remove_element);
        setClickableRemoveImageButton(mLinearLayoutContainerElements, mImageButtonRemoveElement);
        mImageButtonRemoveElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayoutContainerElements.removeViewAt(mLinearLayoutContainerElements.getChildCount() - 2);

                setClickableRemoveImageButton(mLinearLayoutContainerElements, mImageButtonRemoveElement);

                if (mRecipeElements.size() != 0)
                    mRecipeElements.remove(mRecipeElements.size() - 1);
            }
        });

        mLinearLayoutContainerSteps = (LinearLayout) view.findViewById(R.id.list_view_step_container);

        mImageButtonAddStep = (ImageButton) view.findViewById(R.id.kitchen_button_add_step);
        mImageButtonAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RecipeStep step = new RecipeStep();

                LinearLayout anotherLL = (LinearLayout) LayoutInflater.from(getActivity())
                                    .inflate(R.layout.list_item_recipe_step_new, null);
                anotherLL.setId(View.generateViewId());

                mLinearLayoutContainerSteps.addView(anotherLL, mLinearLayoutContainerSteps.getChildCount() - 1);

                TextView mTextViewStepCount = (TextView) anotherLL.findViewById(R.id.recipe_step_number_textView);
                step.setNum(stepCount);
                mTextViewStepCount.setText(getResources().getString(R.string.step_number, stepCount));
                stepCount++;

                EditText mEditTextStepTitle = (EditText) anotherLL.findViewById(R.id.recipe_step_title_editText);
                mEditTextStepTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        step.setName(s.toString());
                        updateArrayOfChilds(step, mRecipeSteps);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                setClickableRemoveImageButton(mLinearLayoutContainerSteps, mImageButtonRemoveStep);
            }
        });

        mImageButtonRemoveStep = (ImageButton) view.findViewById(R.id.kitchen_button_remove_step);
        setClickableRemoveImageButton(mLinearLayoutContainerSteps, mImageButtonRemoveStep);
        mImageButtonRemoveStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayoutContainerSteps.removeViewAt(mLinearLayoutContainerSteps.getChildCount() - 2);

                setClickableRemoveImageButton(mLinearLayoutContainerSteps, mImageButtonRemoveStep);

                if (mRecipeSteps.size() != 0)
                    mRecipeSteps.remove(mRecipeSteps.size() - 1);

                stepCount--;
            }
        });


        return view;
    }

    //todo delete? need test
    private void setClickableRemoveImageButton() {
        //todo need change enabled style
        if (mLinearLayoutContainerElements.getChildCount() == 1) {
            mImageButtonRemoveElement.setClickable(false);
            mImageButtonRemoveElement.setEnabled(false);
        }
        else {
            mImageButtonRemoveElement.setClickable(true);
            mImageButtonRemoveElement.setEnabled(true);
        }
    }
    //todo test
    private void setClickableRemoveImageButton(LinearLayout linearLayout, ImageButton button) {
        if (linearLayout.getChildCount() == 1) {
            button.setClickable(false);
            button.setEnabled(false);
        }
        else {
            button.setClickable(true);
            button.setEnabled(true);
        }
    }

    //todo
    private <T extends RecipeChild> void updateArrayOfChilds(T element, ArrayList<T> array) {
        if (!array.contains(element)) {
            element.setParentRecipeUUID(mRecipe.getId().toString());
            array.add(element);
        }
    }
}
