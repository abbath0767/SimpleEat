package com.luxary_team.simpleeat.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luxary_team.simpleeat.MenuFragment;
import com.luxary_team.simpleeat.R;
import com.luxary_team.simpleeat.interfaces.SelectItemDrawerCallback;
import com.luxary_team.simpleeat.objects.Recipe;
import com.luxary_team.simpleeat.objects.RecipeElement;
import com.luxary_team.simpleeat.objects.RecipeElementLab;
import com.luxary_team.simpleeat.objects.RecipeLab;
import com.luxary_team.simpleeat.objects.RecipeStep;
import com.luxary_team.simpleeat.objects.RecipeStepLab;

import java.util.ArrayList;

import static com.luxary_team.simpleeat.Utils.StepUtilModifier.setClickableRemoveImageButton;
import static com.luxary_team.simpleeat.Utils.StepUtilModifier.updateArrayOfChilds;

public class KitchenStep3Fragment extends Fragment {

    public static final String PARENT = "parent";
    public static final String RECIPE_ELEMENTS = "elements";

    private ImageButton mImageButtonAddStep;
    private ImageButton mImageButtonRemoveStep;
    private Button mButtonSaveRecipe;
    private Button mButtonBack;
    private LinearLayout mLinearLayoutContainerSteps;

    private Recipe mRecipe;
    private ArrayList<RecipeElement> mRecipeElements;
    private ArrayList<RecipeStep> mRecipeSteps;

    private SelectItemDrawerCallback mCallback;

    private int stepCount = 1;

    public static KitchenStep3Fragment newInstance(Recipe recipe, ArrayList<RecipeElement> recipeElements) {
        KitchenStep3Fragment fragment = new KitchenStep3Fragment();
        Bundle args = new Bundle();
        args.putSerializable(PARENT, recipe);
        args.putSerializable(RECIPE_ELEMENTS, recipeElements);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = (Recipe) getArguments().getSerializable(PARENT);
        mRecipeElements = (ArrayList<RecipeElement>) getArguments().getSerializable(RECIPE_ELEMENTS);
        mRecipeSteps = new ArrayList<>();
        mCallback = (SelectItemDrawerCallback) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.kitchen_step3_fragment, container, false);

        mLinearLayoutContainerSteps = (LinearLayout) rootView.findViewById(R.id.list_view_step_container);

        mImageButtonAddStep = (ImageButton) rootView.findViewById(R.id.kitchen_button_add_step);
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
                        updateArrayOfChilds(step, mRecipeSteps, mRecipe);
                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                setClickableRemoveImageButton(mLinearLayoutContainerSteps, mImageButtonRemoveStep);
            }
        });

        mImageButtonRemoveStep = (ImageButton) rootView.findViewById(R.id.kitchen_button_remove_step);
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

        mButtonSaveRecipe = (Button) rootView.findViewById(R.id.kitchen_step_3_save_button);
        mButtonSaveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeLab.get(getActivity()).addRecipe(mRecipe);

                RecipeStepLab.get(getActivity()).setAndSaveRecipeSteps(mRecipeSteps);
                RecipeElementLab.get(getActivity()).setAndSaveRecipeElements(mRecipeElements);
                Toast.makeText(getActivity(), "Новый рецепт создан успешно!", Toast.LENGTH_SHORT).show();

                mCallback.selectItemInDrawer(0);

                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new MenuFragment()).commit();
            }
        });

        mButtonBack = (Button) rootView.findViewById(R.id.kitchen_step_3_back_button);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }
}
