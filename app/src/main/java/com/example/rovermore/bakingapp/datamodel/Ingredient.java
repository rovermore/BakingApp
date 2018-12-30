package com.example.rovermore.bakingapp.datamodel;

public class Ingredient {

    private int quantity;
    private String measure;
    private String ingredient;

    public Ingredient(int quantity, String measure, String ingredients){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredients;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
