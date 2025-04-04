package dev.whodes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.whodes.model.Follower;
import dev.whodes.model.StringListData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * FollowerService is responsible for loading and managing follower data for users.
 * It reads follower data from JSON files stored in a specified directory.
 */
@ApplicationScoped
public class FollowerService {

    private final ObjectMapper objectMapper;
    private final String connectionsDirectory;
    private final String storageDirectory;
    private final static String FOLLOWER_FILE_PATTERN = "followers_.*\\.json";
    private final static Logger LOGGER = Logger.getLogger(FollowerService.class.getName());

    @Inject
    public FollowerService(
            @ConfigProperty(name = "storage.directory") String storageDirectory,
            @ConfigProperty(name = "connections.directory") String connectionsDirectory,
            ObjectMapper objectMapper) {
        this.storageDirectory = storageDirectory;
        this.connectionsDirectory = connectionsDirectory;
        this.objectMapper = objectMapper;
    }


    /**
     * Get the usernames of users that are following the specified user.
     * @param uuid the UUID of the user
     * @return a set of usernames that are following the user
     */
    public Set<String> getUsernames(String uuid) {
        List<Follower> followers = loadFollowers(uuid);
        LOGGER.infof("Loaded %d follower(s) for user: %s", followers.size(), uuid);
        return followers.stream()
                .flatMap(follower -> follower.stringListData().stream())
                .map(StringListData::value)
                .collect(Collectors.toSet());
    }


    private List<Follower> loadFollowers(String user) {
        List<Follower> followerList = new ArrayList<>();
        Path userDirectoryPath = Paths.get(storageDirectory, user, connectionsDirectory);

        if (!Files.exists(userDirectoryPath) || !Files.isDirectory(userDirectoryPath)) {
            LOGGER.warnf("No directory found for user: %s at path: %s", user, userDirectoryPath);
            return followerList;
        }

        DirectoryStream.Filter<Path> filter = entry ->
                Files.isRegularFile(entry) && entry.getFileName().toString().matches(FOLLOWER_FILE_PATTERN);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(userDirectoryPath, filter)) {
            for (Path entry : stream) {
                try {
                    List<Follower> followers = objectMapper.readValue(entry.toFile(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, Follower.class));
                    followerList.addAll(followers);
                    LOGGER.infof("Loaded %d follower(s) from file: %s", followers.size(), entry.getFileName());
                } catch (IOException e) {
                    LOGGER.warnf(e, "Failed to parse followers from file: %s", entry.getFileName());
                }
            }
        } catch (IOException e) {
            LOGGER.errorf(e, "Error reading follower directory for user: %s", user);
        }

        return followerList;
    }

}
