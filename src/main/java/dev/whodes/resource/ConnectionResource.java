package dev.whodes.resource;


import dev.whodes.dto.FollowersResponse;
import dev.whodes.service.ConnectionService;
import io.quarkus.cache.CacheResult;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.Set;

/**
 * REST resource for managing user connections.
 * This class provides endpoints to get the list of users that a user is following,
 * the list of users that are following a user, and the difference between these two lists.
 */
@Path("api/connections")
public class ConnectionResource {

    @Inject
    ConnectionService connectionService;


    @GET
    @Produces("application/json")
    @Path("{uuid}/following")
    public Response getFollowing(@PathParam("uuid") String uuid) {
        Set<String> following = connectionService.getFollowingUserNames(uuid);
        if (following == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found or unable to retrieve data").build();
        }
        return Response.ok(new FollowersResponse(following)).build();
    }

    @GET
    @Produces("application/json")
    @Path("{uuid}/followers")
    public Response getFollowers(@PathParam("uuid") String uuid) {
        Set<String> followers = connectionService.getFollowersUserNames(uuid);
        if (followers == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found or unable to retrieve data").build();
        }
        return Response.ok(new FollowersResponse(followers)).build();
    }

    @GET
    @Produces("application/json")
    @Path("{uuid}/followingNotReturned")
    public Response getFollowingNotReturned(@PathParam("uuid") String uuid) {
        Set<String> followingNotReturned = connectionService.getFollowersButNotFollowing(uuid);
        return Response.ok(new FollowersResponse(followingNotReturned)).build();
    }

    @GET
    @Produces("application/json")
    @Path("{uuid}/usersNotFollowingBack")
    @CacheResult(cacheName = "not-following")
    public Response getNotFollowers(@PathParam("uuid") String uuid) {
        Set<String> notFollowingBack = connectionService.getFollowingButNotFollowers(uuid);
        return Response.ok(new FollowersResponse(notFollowingBack)).build();
    }

}
