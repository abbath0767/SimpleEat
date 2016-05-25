package com.luxary_team.simpleeat;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luxary_team.simpleeat.objects.Recipe;
import com.luxary_team.simpleeat.objects.RecipeLab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BasketFragment extends Fragment {

    private ArrayList<Recipe> mRecipes;
    private RecipeLab mRecipeLab;

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipes = new ArrayList<>();

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        mRecipeLab = RecipeLab.get(getActivity());

        for (Map.Entry<String, ?> iter: preferences.getAll().entrySet())
            if (iter.getKey().substring(0, 6).equals("Parent"))
                mRecipes.add(mRecipeLab.getRecipe(UUID.fromString(
                        iter.getKey().substring(7, iter.getKey().length()))));

        Log.d(MainActivity.TAG, "all Recipes: " + mRecipes);

        mRecipeAdapter = new RecipeAdapter(mRecipes);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basket_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.basket_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecipeAdapter);

        return rootView;
    }

    private class RecipeViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView title;
        private Recipe recipe;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.basket_fragment_card_view);
            title = (TextView) itemView.findViewById(R.id.basket_fragment_card_view_text_view_title);
        }

        public void onBindRecipeViewHolder(Recipe recipe) {
            this.recipe = recipe;
            title.setText(recipe.getTitle());
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
            RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);
            return recipeViewHolder;
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

}
