package dev.whodes.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Service to manage connections between users.
 * It provides methods to get the list of users that a user is following,
 * the list of users that are following a user, and the difference between
 * these two lists.
 */
@ApplicationScoped
public class ConnectionService {

    private final FollowingService followingService;
    private final FollowerService followersService;
    private final static Logger LOGGER = Logger.getLogger(ConnectionService.class.getName());

    @Inject
    public ConnectionService(
            FollowingService followingService,
            FollowerService followersService) {
        this.followingService = followingService;
        this.followersService = followersService;
    }


    /**
     * Get the list of users that a user is following.
     *
     * @param uuid the UUID of the user
     * @return a set of usernames that the user is following
     */
    public Set<String> getFollowingUserNames(String uuid) {
        LOGGER.info("Getting following for user: " + uuid);
        return followingService.getUsernames(uuid);
    }

    /**
     * Get the list of users that are following a user.
     *
     * @param uuid the UUID of the user
     * @return a set of usernames that are following the user
     */
    public Set<String> getFollowersUserNames(String uuid) {
        LOGGER.info("Getting followers for user: " + uuid);
        return followersService.getUsernames(uuid);
    }

    /**
     * Get the list of users that a user is following but are not following back.
     *
     * @param uuid the UUID of the user
     * @return a set of usernames that the user is following but are not following back
     */
    public Set<String> getFollowingButNotFollowers(String uuid) {
        LOGGER.info("Getting following but not followers for user: " + uuid);
        Set<String> following = new HashSet<>(followingService.getUsernames(uuid));
        following.removeAll(followersService.getUsernames(uuid));
        return following;
    }

    /**
     * Get the list of users that are following a user but the user is not following back.
     * @param uuid the UUID of the user
     * @return a set of usernames that are following the user but the user is not following back
     */
    public Set<String> getFollowersButNotFollowing(String uuid) {
        LOGGER.info("Getting followers but not following for user: " + uuid);
        Set<String> followers =  new HashSet<>(followersService.getUsernames(uuid));
        followers.removeAll(followingService.getUsernames(uuid));
        return followers;
    }
}
