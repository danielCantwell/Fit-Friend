//package com.cantwellcode.athletejournal.Nutrition;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//
//import com.cantwellcode.athletejournal.Connect.Friends.Comment;
//import com.cantwellcode.athletejournal.Connect.User;
//
//import java.util.List;
//
///**
// * Created by Daniel on 4/27/2014.
// */
//public class NutritionPost {
//
//    private String title;
//    private Bitmap image;
//    private String calories;
//    private String fat;
//    private String carbs;
//    private String protein;
//    private String preparation;
//
//    public NutritionPost(User author, String group, String dateTime, String content, Bundle options, String title, String calories, String fat, String carbs, String protein, String preparation) {
//        super(author, group, dateTime, content, options);
//        this.title = title;
//        this.calories = calories;
//        this.fat = fat;
//        this.carbs = carbs;
//        this.protein = protein;
//        this.preparation = preparation;
//    }
//
//    public NutritionPost(User author, String group, String dateTime, String content, List<User> highFives, List<Comment> discussion, Bundle options, String title, String calories, String fat, String carbs, String protein, String preparation) {
//        super(author, group, dateTime, content, highFives, discussion, options);
//        this.title = title;
//        this.calories = calories;
//        this.fat = fat;
//        this.carbs = carbs;
//        this.protein = protein;
//        this.preparation = preparation;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public Bitmap getImage() {
//        return image;
//    }
//
//    public void setImage(Bitmap image) {
//        this.image = image;
//    }
//
//    public String getCalories() {
//        return calories;
//    }
//
//    public void setCalories(String calories) {
//        this.calories = calories;
//    }
//
//    public String getFat() {
//        return fat;
//    }
//
//    public void setFat(String fat) {
//        this.fat = fat;
//    }
//
//    public String getCarbs() {
//        return carbs;
//    }
//
//    public void setCarbs(String carbs) {
//        this.carbs = carbs;
//    }
//
//    public String getProtein() {
//        return protein;
//    }
//
//    public void setProtein(String protein) {
//        this.protein = protein;
//    }
//
//    public String getPreparation() {
//        return preparation;
//    }
//
//    public void setPreparation(String preparation) {
//        this.preparation = preparation;
//    }
//}
