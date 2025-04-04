package dev.whodes.service;


import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * UnZipperService is responsible for unzipping files.
 * It creates a unique directory for each unzipped file and ensures that the
 * unzipped files are stored in a safe location to prevent zip slip attacks.
 */
@ApplicationScoped
public class UnZipperService {

    private final String STORAGE_DIR;

    public UnZipperService(@ConfigProperty(name = "storage.directory") String storageDirectory) {
        this.STORAGE_DIR = storageDirectory;
    }


    /**
     * Unzips a zip file to a unique directory.
     * @param zipFile the zip file to unzip
     * @return the UUID of the directory where the files were unzipped
     * @throws IOException if an I/O error occurs
     */
    public String unzip(File zipFile) throws IOException  {
        String uuid = UUID.randomUUID().toString();
        File destinationDir = new File(STORAGE_DIR + File.separator + uuid);
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry ze = zis.getNextEntry();
        while( ze != null ) {
            File newFile = newFile(destinationDir, ze);
            if(ze.isDirectory()) {
                newFile.mkdirs();
            } else {
                File parent = newFile.getParentFile();
                if(!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory: " + parent);
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while (( len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            ze = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        return uuid;
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException{
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            // protects against zip slip
            throw new RuntimeException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

}
