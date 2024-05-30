package com.stemgon.autheply.webApi.models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class User {
    private UUID id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String email;
    private String username;
    private String password;

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User( String email, String username,String firstName, String lastName, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
