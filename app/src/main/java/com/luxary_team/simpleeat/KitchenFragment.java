package com.luxary_team.simpleeat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luxary_team.simpleeat.Utils.PermissionRequester;
import com.luxary_team.simpleeat.Utils.PhotoWorker;
import com.luxary_team.simpleeat.Utils.ThumbnailRotator;
import com.luxary_team.simpleeat.interfaces.SelectItemDrawerCallback;
import com.luxary_team.simpleeat.objects.Recipe;
import com.luxary_team.simpleeat.objects.RecipeChild;
import com.luxary_team.simpleeat.objects.RecipeElement;
import com.luxary_team.simpleeat.objects.RecipeElementLab;
import com.luxary_team.simpleeat.objects.RecipeLab;
import com.luxary_team.simpleeat.objects.RecipeStep;
import com.luxary_team.simpleeat.objects.RecipeStepLab;
import com.luxary_team.simpleeat.objects.RecipeType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

//todo DELETE CLASS
public class KitchenFragment extends Fragment {

    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_SELECT_FILE = 4;

    private EditText mTitleEditText;
    private Spinner mTypeSpinner;
    private Button mAddRecipeButton;
    private LinearLayout mLinearLayoutContainerElements;
    private LinearLayout mLinearLayoutContainerSteps;
    private ImageButton mImageButtonAddElement;
    private ImageButton mImageButtonRemoveElement;
    private ImageButton mImageButtonAddStep;
    private ImageButton mImageButtonRemoveStep;
    private ImageButton mImageButtonTakePhoto;
    private ImageView mImageViewPhoto;

    private RecipeLab mRecipeLab;
    private RecipeElementLab mRecipeElementLab;
    private RecipeStepLab mRecipeStepLab;
    private RecipeType mRecipeType;
    private RecipeType[] recipeTypesWithoutFavorite;
    private Recipe mRecipe;
    private ArrayList<RecipeElement> mRecipeElements;
    private ArrayList<RecipeStep> mRecipeSteps;
    private SelectItemDrawerCallback mCallback;

    private String userChoosen = "";
    private File mPhotoFile;
    private Intent captureImageIntent;

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
        mPhotoFile = mRecipeLab.getPhotoFile(mRecipe);

        mCallback = (SelectItemDrawerCallback) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kitchen_fragment, container, false);

        //remove last element
        recipeTypesWithoutFavorite = RecipeType.values();
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
        ArrayAdapter<RecipeType> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_expandable_list_item_1, recipeTypesWithoutFavorite);
        mTypeSpinner.setAdapter(adapter);
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int position, long selectedId) {

                RecipeType recipe = (RecipeType) parent.getAdapter().getItem(position);

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
                for (RecipeStep step : mRecipeSteps)
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

        mImageButtonTakePhoto = (ImageButton) view.findViewById(R.id.kitchen_photo_take_button);
        mImageButtonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mImageViewPhoto = (ImageView) view.findViewById(R.id.kitchen_photo_image_view);

        return view;
    }

    private void selectImage() {
        final String[] mAnswers = getResources().getStringArray(R.array.photo_answer);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Добавить фото рецепта") //todo unhardcode
                .setItems(R.array.photo_answer, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean perm = PermissionRequester.checkPermission(getActivity()); //todo make this
                        switch (which) {
                            case 0:
                                userChoosen = mAnswers[which];
                                if (perm)
                                    cameraIntent();
                                break;
                            case 1:
                                userChoosen = mAnswers[which];
                                if (perm)
                                    galleryIntent();
                                break;
                            case 2:
                                dialog.dismiss();
                        }
                    }
                });

        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //todo unhardcode
        startActivityForResult(intent.createChooser(intent, "Выберите файл"),
                REQUEST_SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == REQUEST_SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                PhotoWorker.bitmapToFile(bm, mPhotoFile);
                bm = ThumbnailRotator.rotateThumbnail(bm, mPhotoFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mImageViewPhoto.setImageResource(android.R.color.transparent);
        mImageViewPhoto.setImageBitmap(bm);
        mImageViewPhoto.setBackgroundColor(Color.WHITE);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        PhotoWorker.bitmapToFile(thumbnail, mPhotoFile);

        mImageViewPhoto.setImageResource(android.R.color.transparent);
        mImageViewPhoto.setImageBitmap(thumbnail);
        mImageViewPhoto.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionRequester.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosen.equals(getResources().getStringArray(R.array.photo_answer)[0]))
                        cameraIntent();
                    else if (userChoosen.equals(getResources().getStringArray(R.array.photo_answer)[0]))
                        galleryIntent();
                } else {
                    Toast.makeText(getActivity(), "oops, =( permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
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

    private <T extends RecipeChild> void updateArrayOfChilds(T element, ArrayList<T> array) {
        if (!array.contains(element)) {
            element.setParentRecipeUUID(mRecipe.getId().toString());
            array.add(element);
        }
    }
}
