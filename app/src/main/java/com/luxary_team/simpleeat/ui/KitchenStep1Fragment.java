package com.luxary_team.simpleeat.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luxary_team.simpleeat.MainActivity;
import com.luxary_team.simpleeat.R;
import com.luxary_team.simpleeat.Utils.PermissionRequester;
import com.luxary_team.simpleeat.Utils.PhotoWorker;
import com.luxary_team.simpleeat.Utils.ThumbnailRotator;
import com.luxary_team.simpleeat.interfaces.SelectItemDrawerCallback;
import com.luxary_team.simpleeat.objects.Recipe;
import com.luxary_team.simpleeat.objects.RecipeLab;
import com.luxary_team.simpleeat.objects.RecipeType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class KitchenStep1Fragment extends Fragment {

    public static final String PARENT = "parent";
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_SELECT_FILE = 4;
    private int portionCount = 0;
    private int hour = 0;
    private int minuts = 0;
    private String mRecipeTime = "0:00";

    private Recipe mRecipe;
    private RecipeLab mRecipeLab;
//    private String mNewRecipeUUIDStr;
    private String userChoosen = "";
    private RecipeType[] recipeTypesWithoutFavorite;
    private RecipeType mRecipeType;

    private EditText mEditTextTitle;
    private Button mButtonNext;
    private ImageButton mImageButton;
    private ImageView mImageViewPhoto;
    private SelectItemDrawerCallback mCallback;
    private File mPhotoFile;
    private Spinner mTypeSpinner;
    private TextView mPortionCountTextView;
    private ImageButton mPlusImageButton;
    private ImageButton mMinusImageButton;
    private NumberPicker mHourNumberPicker;
    private NumberPicker mMinute1NumberPicker;

    public static KitchenStep1Fragment newInstance(Recipe recipe) {
        //todo make Recipe impl Parcelable or Serializable
        //todo i make this! now Recipe impl Serializable
        KitchenStep1Fragment fragment = new KitchenStep1Fragment();
        Bundle args = new Bundle();
        args.putSerializable(PARENT, recipe);
        fragment.setArguments(args);

        Log.d(MainActivity.TAG, "newInstance recipe UUID = " + recipe.getId().toString());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipeLab = RecipeLab.get(getActivity());
        mRecipe = (Recipe) getArguments().getSerializable(PARENT);
        mPhotoFile = mRecipeLab.getPhotoFile(mRecipe);
        recipeTypesWithoutFavorite = RecipeType.values();
        recipeTypesWithoutFavorite = Arrays.copyOf(recipeTypesWithoutFavorite, recipeTypesWithoutFavorite.length - 1);

        mCallback = (SelectItemDrawerCallback) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.kitchen_step1_fragment, container, false);

        mEditTextTitle = (EditText) rootView.findViewById(R.id.new_recipe_step1_title_edit_text);
        mButtonNext = (Button) rootView.findViewById(R.id.kitchen_step_1_next_button);
        mImageButton = (ImageButton) rootView.findViewById(R.id.kitchen_step_1_photo_take_button);
        mImageViewPhoto = (ImageView) rootView.findViewById(R.id.kitchen_step_1_photo_image_view);
        mPortionCountTextView = (TextView) rootView.findViewById(R.id.potion_count_text_view);
        mPlusImageButton = (ImageButton) rootView.findViewById(R.id.portion_plus);
        mMinusImageButton = (ImageButton) rootView.findViewById(R.id.portion_minus);
        mHourNumberPicker = (NumberPicker) rootView.findViewById(R.id.kitchen_hour_number_picker);
        mMinute1NumberPicker = (NumberPicker) rootView.findViewById(R.id.kitchen_minute1_number_picker);

        mEditTextTitle.addTextChangedListener(new TextWatcher() {
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

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                hidePhotoIcon();
            }
        });

        mImageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                hidePhotoIcon();
            }
        });

        mTypeSpinner = (Spinner) rootView.findViewById(R.id.new_recipe_type_spinner);
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

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = String.valueOf(minuts);
                tmp = tmp.length() == 1 ? ("0" + tmp) : tmp;

                mRecipeTime = hour + ":" + tmp;
                mRecipe.setRecipeTime(mRecipeTime);
                mRecipe.setPortionCount(portionCount);
                Log.d(MainActivity.TAG, "recipe coock time = " + mRecipeTime);
                Log.d(MainActivity.TAG, "recipe portions count = " + portionCount);
                KitchenStep2Fragment step2Fragment = KitchenStep2Fragment.newInstance(mRecipe);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, step2Fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        mPlusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portionCount++;
                portionCountChange();
            }
        });

        mMinusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portionCount--;
                portionCountChange();
            }
        });

        mHourNumberPicker.setMinValue(0);
        mHourNumberPicker.setMaxValue(23);
        mHourNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hour = newVal;
            }
        });
        mMinute1NumberPicker.setMaxValue(0);
        mMinute1NumberPicker.setMaxValue(59);
        mMinute1NumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minuts = newVal;
            }
        });

        return rootView;
    }

    private void hidePhotoIcon() {
        if (mImageViewPhoto.getDrawable() == null)
            mImageButton.setVisibility(View.GONE);
    }

    private void portionCountChange() {
        if (portionCount < 0)
            portionCount++;

        mPortionCountTextView.setText(String.valueOf(portionCount));
    }

    private void selectImage() {
        final String[] mAnswers = getResources().getStringArray(R.array.photo_answer);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Добавить фото рецепта") //todo unhardcode
                .setItems(R.array.photo_answer, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean perm = PermissionRequester.checkPermission(getActivity());
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
}
