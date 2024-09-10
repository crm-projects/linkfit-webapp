package com.server.storefront.constants;

public class GraphAPI {

    private GraphAPI() {}
    public static final String BASE_URL = "https://graph.instagram.com";
    public static final String API_VERSION = "/v20.0";
    public static final String ME = "me";
    public static final String USER_PROFILE_FIELDS = "user_id,username,name,account_type,profile_picture_url,followers_count,follows_count,media_count";
    public static final String USER_MEDIA_FIELDS = "id,media_type,thumbnail_url";
    public static final String IG_FIELDS = "fields=";
    public static final String IG_MEDIA = "media";
    public static final String USER_ACCESS_TOKEN = "access_token";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String GRANT_TYPE = "grant_type";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String IG_OAUTH_URI = "oauth/access_token";
}
