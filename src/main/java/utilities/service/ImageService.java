package utilities.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageService {

    private static final String UPLOAD_DIR = "src/main/resources/images/inventory/";

    /**
     * Saves an image into the project resources folder.
     *
     * @param sourceFile the image selected by the user
     * @return database-friendly image path
     */
    public static String saveImage(File sourceFile) {

        try {

            File directory = new File(UPLOAD_DIR);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + sourceFile.getName();

            Path targetPath = Paths.get(UPLOAD_DIR + uniqueFileName);

            Files.copy(
                    sourceFile.toPath(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "/images/inventory/" + uniqueFileName;

        } catch (IOException e) {

            e.printStackTrace();

            return "/images/placeholder.png";
        }
    }

    /**
     * Deletes an image from the inventory folder.
     *
     * @param imagePath database image path
     * @return true if deleted successfully
     */
    public static boolean deleteImage(String imagePath) {

        try {

            String cleanPath = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;

            Path filePath = Paths.get("src/main/resources/" + cleanPath);

            return Files.deleteIfExists(filePath);

        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
    }
}