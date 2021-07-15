package com.example.help;

import com.google.firebase.firestore.Exclude;

public class UsersContent {
    private static String document_id;
    private   String site_name;
    private   String pass;
    private  String description;

    public UsersContent() {
    }

    public UsersContent(String user_name, String pass,String description) {
        this.site_name = user_name;
        this.pass = pass;
        this.description=description;
    }

    public String getSite_name() {
        return site_name;
    }

    public String getPass() {
        return pass;
    }
    public String getDescription(){
        return description;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }


    @Exclude
    public static String getDocument_id() {
        return document_id;
    }




}
