package dev.whodes.dto;

import java.util.Set;

public class FollowersResponse {
    public Set<String> users;

    public FollowersResponse() {}

    public FollowersResponse(Set<String> users) {
        this.users = users;
    }

}
