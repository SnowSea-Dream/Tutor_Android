package com.snowsea.accountingtutors.model;

/**
 * Created by SnowSea on 3/29/2017.
 */

public class User {

    private String first_name;
    private String last_name;
    private String country;
    private String phone_number;
    private String email;
    private int user_type;
    private String password;
    private String created_at;
    private String new_password;
    private String token;
    private String code;

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(int type) {
        this.user_type = type;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getCountry() {
        return country;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public int getUserType() { return this.user_type; }

    public String getCreated_at() {
        return created_at;
    }

    public void setNewPassword(String newPassword) {
        this.new_password = newPassword;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCode(String code) { this.code = code; }

    public String getCode() { return this.code; }
}
