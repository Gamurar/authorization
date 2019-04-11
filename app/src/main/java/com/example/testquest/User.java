package com.example.testquest;

import com.google.gson.annotations.SerializedName;

public class User {
    private String name;
    private String activity;
    private String age;
    @SerializedName("e-mail")
    private String email;


    public User(String name, String activity, String age, String email) {
        this.name = name;
        this.activity = activity;
        this.age = age;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
