package dev.whodes.service;


import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;


@ApplicationScoped
public class CleanupService {

    private static final Logger LOGGER = Logger.getLogger(CleanupService.class);

    private final String storageDirectory;

    public CleanupService(@ConfigProperty( name = "storage.directory") String storageDirectory) {
        this.storageDirectory = storageDirectory;
    }

    @Scheduled(every = "30m")
    public void cleanupOldFiles() {
        Path basePath = Paths.get(storageDirectory);

        try (Stream<Path> files = Files.walk(basePath)) {
            files.filter(Files::isRegularFile) // Only consider regular files
                    .filter(this::isFileOlderThanThreshold) // Only files older than threshold
                    .forEach(path -> {
                        try {
                            Files.delete(path); // Delete the file
                            LOGGER.infof("Deleted file: %s", path);
                        } catch (IOException e) {
                            LOGGER.errorf(e, "Failed to delete file: %s", path);
                        }
                    });
        } catch (IOException e) {
            LOGGER.error("Failed to walk through user file directory for cleanup", e);
        }
    }

    /**
     * Check if the file is older than the defined threshold (10 minutes).
     *
     * @param path the path of the file
     * @return true if the file is older than 10 minutes, false otherwise
     */
    private boolean isFileOlderThanThreshold(Path path) {
        try {
            FileTime fileTime = Files.getLastModifiedTime(path);
            Instant threshold = Instant.now().minus(10, ChronoUnit.MINUTES);
            return fileTime.toInstant().isBefore(threshold);
        } catch (IOException e) {
            LOGGER.warnf(e, "Could not read last modified time for file: %s", path);
            return false;
        }
    }

}
