package com.example.rovermore.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.datamodel.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;
    private Context context;

    public IngredientAdapter(Context context, List<Ingredient> ingredientList ){
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View rootView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ingredient_item_list,viewGroup,false);

        return new IngredientViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, int i) {
        Ingredient ingredient = ingredientList.get(i);
        int quantity = ingredient.getQuantity();
        String quantityText = String.valueOf(quantity);
        ingredientViewHolder.quantityTextView.setText(quantityText);
        ingredientViewHolder.measureTextView.setText(ingredient.getMeasure());
        ingredientViewHolder.ingredientTextView.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        if(ingredientList==null) return 0;
        return ingredientList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView quantityTextView;
        TextView measureTextView;
        TextView ingredientTextView;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            quantityTextView = itemView.findViewById(R.id.tv_quantity);
            measureTextView = itemView.findViewById(R.id.tv_measure);
            ingredientTextView = itemView.findViewById(R.id.tv_ingredient);
        }
    }

    public void setIngredientList(List<Ingredient> ingredientList){
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    public void clearIngredientList(){
        if(ingredientList!=null) {
            ingredientList.clear();
            ingredientList = null;
            notifyDataSetChanged();
        }
    }
}
