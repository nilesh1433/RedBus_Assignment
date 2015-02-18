package com.example.nilesh.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nilesh on 2/18/2015.
 */
public class Owner
{
    String login;
    @SerializedName("avatar_url")
    String avatarUrl;
    @SerializedName("followers_url")
    String followersUrl;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getFollowersUrl() {
        return followersUrl;
    }

    public void setFollowersUrl(String followersUrl) {
        this.followersUrl = followersUrl;
    }
}

