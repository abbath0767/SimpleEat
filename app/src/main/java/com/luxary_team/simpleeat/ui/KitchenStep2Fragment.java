package com.luxary_team.simpleeat.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.luxary_team.simpleeat.MainActivity;
import com.luxary_team.simpleeat.R;
import com.luxary_team.simpleeat.objects.Recipe;
import com.luxary_team.simpleeat.objects.RecipeChild;
import com.luxary_team.simpleeat.objects.RecipeElement;
import com.luxary_team.simpleeat.objects.RecipeLab;

import java.util.ArrayList;

public class KitchenStep2Fragment extends Fragment{

    public static final String PARENT = "parent";

    private Recipe mRecipe;
    private RecipeLab mRecipeLab;
    private ArrayList<RecipeElement> mRecipeElements;

    private LinearLayout mLinearLayoutContainerElements;
    private ImageButton mImageButtonAddElement;
    private ImageButton mImageButtonRemoveElement;
    private Button mButtonNext;


    public static KitchenStep2Fragment newInstance(Recipe recipe) {
        KitchenStep2Fragment fragment = new KitchenStep2Fragment();
        Bundle args = new Bundle();
        args.putSerializable(PARENT, recipe);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = (Recipe) getArguments().getSerializable(PARENT);
        mRecipeLab = RecipeLab.get(getActivity());
        mRecipeElements = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.kitchen_step2_fragment, container, false);

        mLinearLayoutContainerElements = (LinearLayout) rootView.findViewById(R.id.list_view_element_container);

        mImageButtonAddElement = (ImageButton) rootView.findViewById(R.id.kitchen_button_add_element);
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

        mImageButtonRemoveElement = (ImageButton) rootView.findViewById(R.id.kitchen_button_remove_element);
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

        mButtonNext = (Button) rootView.findViewById(R.id.kitchen_step_2_next_button);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.TAG, "next button clicked");
                Log.d(MainActivity.TAG, "Recipe UUID: " + mRecipe.getId().toString());
                Log.d(MainActivity.TAG, "Recipe title: " + mRecipe.getTitle());
                Log.d(MainActivity.TAG, "Recipe type: " + mRecipe.getRecipeType());
                for (RecipeElement elem: mRecipeElements)
                    Log.d(MainActivity.TAG, "element: " + elem.getName() + ", " + elem.getCount());

            }
        });

        return rootView;
    }

    //todo make simple method
    private <T extends RecipeChild> void updateArrayOfChilds(T element, ArrayList<T> array) {
        if (!array.contains(element)) {
            element.setParentRecipeUUID(mRecipe.getId().toString());
            array.add(element);
        }
    }

    private void setClickableRemoveImageButton(LinearLayout linearLayout, ImageButton button) {
        if (linearLayout.getChildCount() == 1) {
            button.setClickable(false);
            button.setEnabled(false);
        } else {
            button.setClickable(true);
            button.setEnabled(true);
        }
    }
}
