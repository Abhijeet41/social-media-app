package com.abhi41.socialmediaapp.untils;

public class Constants {
    public static String BASE_URL = "https://www.googleapis.com/androidcheck/v1/attestations/";

    public static String verify = "verify";

    public static String getAndroidCheck(){
        return BASE_URL + verify;
    }
}
