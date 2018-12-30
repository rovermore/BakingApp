package com.example.rovermore.bakingapp.datamodel;

public class Recipe {

    private int id;
    private String recipeName;

    public Recipe(int id, String recipeName){
        this.id = id;
        this.recipeName = recipeName;
    }

    public int getId() {
        return id;
    }

    public String getRecipeName() {
        return recipeName;
    }
}
