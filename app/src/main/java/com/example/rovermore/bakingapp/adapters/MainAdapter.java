package com.example.rovermore.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.datamodel.Recipe;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyRecipeViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    private onRecipeClickListener mOnRecipeClickListener;

    public MainAdapter(Context context, List<Recipe> recipeList, onRecipeClickListener mOnRecipeClickListener){
        this.context = context;
        this.recipeList = recipeList;
        this.mOnRecipeClickListener = mOnRecipeClickListener;
    }

    public interface onRecipeClickListener{
        void onRecipeClicked(Recipe recipe);
    }

    @NonNull
    @Override
    public MainAdapter.MyRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_item_list,viewGroup,false);
        return new MyRecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MyRecipeViewHolder myRecipeViewHolder, int i) {

        Recipe recipe = recipeList.get(i);

        myRecipeViewHolder.nameTextView.setText(recipe.getRecipeName());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class MyRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameTextView;

        public MyRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_recipe_name);
            nameTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Recipe recipe = recipeList.get(position);
            mOnRecipeClickListener.onRecipeClicked(recipe);
        }
    }
}
