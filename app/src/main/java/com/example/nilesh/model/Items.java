package com.example.nilesh.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nilesh on 2/18/2015.
 *
 * For mapping JSON response to JAVA POJO using Gson
 */
public class Items
{
    @SerializedName("name")
    String reposName;
    int watchersCount;
    int forksCount;
    Owner owner;
    @SerializedName("git_url")
    String gitUrl;
    int follwerCount = -1;

    public String getReposName() {
        return reposName;
    }

    public void setReposName(String reposName) {
        this.reposName = reposName;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public int getFollwerCount() {
        return follwerCount;
    }

    public void setFollwerCount(int follwerCount) {
        this.follwerCount = follwerCount;
    }
}