package dev.whodes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.whodes.model.Following;
import dev.whodes.model.StringListData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * FollowingService is responsible for loading and managing following data for users.
 */
@ApplicationScoped
public class FollowingService {

    private final ObjectMapper objectMapper;
    private final String connectionsDirectory;
    private final String storageDirectory;
    private final static Logger LOGGER = Logger.getLogger(FollowingService.class.getName());


    @Inject
    public FollowingService(
            @ConfigProperty(name = "storage.directory") String storageDirectory,
            @ConfigProperty(name = "connections.directory") String connectionsDirectory,
            ObjectMapper objectMapper) {
        this.storageDirectory = storageDirectory;
        this.connectionsDirectory = connectionsDirectory;
        this.objectMapper = objectMapper;
    }

    /**
     * Get the usernames of users that the specified user is following.
     * @param uuid the UUID of the user
     * @return a set of usernames that the user is following
     */
    public Set<String> getUsernames(String uuid) {
        Following followingList = loadFollowing(uuid);
        LOGGER.info("Loaded following data for user: " + uuid);
        return followingList.relationshipsFollowing()
                .stream()
                .flatMap(following -> following.stringListData().stream())
                .map(StringListData::value)
                .collect(Collectors.toSet());
    }

    private Following loadFollowing(String user) {
        Path followingPath = Paths.get(storageDirectory, user, connectionsDirectory, "following.json");
        File followingFile = followingPath.toFile();

        LOGGER.infof("Loading following data from: %s", followingPath);

        if (!followingFile.exists()) {
            LOGGER.warnf("Following file not found for user: %s", user);
            return new Following(List.of());
        }
        try {
            return objectMapper.readValue(followingFile, Following.class);
        } catch (IOException e) {
            LOGGER.errorf(e, "Error reading following data for user: %s", user);
        }

        return new Following(List.of());
    }
}
