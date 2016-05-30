package com.luxary_team.simpleeat;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luxary_team.simpleeat.objects.Recipe;
import com.luxary_team.simpleeat.objects.RecipeElement;
import com.luxary_team.simpleeat.objects.RecipeElementLab;
import com.luxary_team.simpleeat.objects.RecipeLab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BasketFragment extends Fragment {

    private ArrayList<Recipe> mRecipes;
    private RecipeLab mRecipeLab;
    private RecipeElementLab mRecipeElementLab;
    private SharedPreferences preferences;

    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private RecipeAdapter mRecipeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipes = new ArrayList<>();

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        mRecipeLab = RecipeLab.get(getActivity());
        mRecipeElementLab = RecipeElementLab.get(getActivity());

        for (Map.Entry<String, ?> iter : preferences.getAll().entrySet())
            if (iter.getKey().substring(0, 6).equals("Parent"))
                mRecipes.add(mRecipeLab.getRecipe(UUID.fromString(
                        iter.getKey().substring(7, iter.getKey().length()))));

        mRecipeAdapter = new RecipeAdapter(mRecipes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basket_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.basket_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecipeAdapter);

        mEmptyTextView = (TextView) rootView.findViewById(R.id.basket_fragment_empty_text_view);

        notifyEmptyView();

        return rootView;
    }

    private class RecipeViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView; //todo unprofit parametr
        private TextView recipeTitle;
        private LinearLayout container;
        private ArrayList<RecipeElement> elementList;
        private ImageButton deleteButton;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.basket_fragment_card_view);
            recipeTitle = (TextView) itemView.findViewById(R.id.basket_fragment_card_view_text_view_title);
            container = (LinearLayout) itemView.findViewById(R.id.basket_card_view_element_container);
            deleteButton = (ImageButton) itemView.findViewById(R.id.basket_card_view_delete_button);
        }

        public void onBindRecipeViewHolder(Recipe recipe) {
            recipeTitle.setText(recipe.getTitle());
            elementList = mRecipeElementLab.getRecipeElements(recipe.getId().toString());

            for (RecipeElement elem : elementList) {

                LinearLayout anotherElementLL = (LinearLayout) LayoutInflater.from(getActivity())
                        .inflate(R.layout.basket_card_view_element_item, null);
                final TextView elemName = (TextView) anotherElementLL.findViewById(R.id.basket_fragment_card_view_layout_text_view_ingr_name);

                anotherElementLL.setId(View.generateViewId());

                container.addView(anotherElementLL);
                
                elemName.setText(elem.getName() + " / " + elem.getCount());

                CheckBox checkBox = (CheckBox) anotherElementLL.findViewById(R.id.basket_fragment_card_view_layout_check_box);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            elemName.setPaintFlags(elemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            elemName.setTextColor(Color.GRAY);
                        } else {
                            elemName.setPaintFlags(0);
                            elemName.setTextColor(Color.BLACK);
                        }
                    }
                });
            }

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeCardFromBasket(getAdapterPosition());
                }
            });

        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
        private List<Recipe> recipes;

        public RecipeAdapter(List<Recipe> recipes) {
            this.recipes = recipes;
        }

        @Override
        public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.basket_card_view, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeViewHolder holder, int position) {
            Recipe recipe = recipes.get(position);
            holder.onBindRecipeViewHolder(recipe);
        }

        @Override
        public int getItemCount() {
            return recipes.size();
        }

        public void setRecipes(ArrayList<Recipe> recipes) {
            this.recipes = recipes;
        }
    }

    private  void removeCardFromBasket(int position) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("Parent:" + mRecipes.get(position).getId().toString());
        editor.apply();

        mRecipeAdapter.recipes.remove(position);
        mRecipeAdapter.notifyItemRemoved(position);
        mRecipeAdapter.notifyItemRangeChanged(position, mRecipeAdapter.recipes.size());

        notifyEmptyView();
    }

    private void notifyEmptyView() {
        if (mRecipes.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);
        }
    }
}
