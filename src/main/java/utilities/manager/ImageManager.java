package utilities.manager;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageManager {

    private static final String UPLOAD_EQUIPMENT = "src/main/resources/images/inventory/";

    /**
     * Saves an image into the project resources folder.
     *
     * @param sourceFile the image selected by the user
     * @return database-friendly image path
     */
    public static String saveImage(File sourceFile) {

        try {

            File directory = new File(UPLOAD_EQUIPMENT);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + sourceFile.getName();

            Path targetPath = Paths.get(UPLOAD_EQUIPMENT + uniqueFileName);

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

    public static Image getSafeImage(String path, double width, double height) {
        // 1. Check if the String itself is valid
        if (path == null || path.isBlank()) {
            return getPlaceholder(width, height);
        }

        // 2. Check if the file actually exists in your resources
        // getClass().getResource() returns null if the path is invalid
        var resource = ImageManager.class.getResource(path);

        if (resource == null) {
            System.err.println("IMAGE NOT FOUND: " + path);
            return getPlaceholder(width, height);
        }

        // 3. Load the image now that we know the URL is safe
        // (Using the 6-parameter constructor for performance)
        return new Image(resource.toExternalForm(), width, height, true, true, true);
    }

    private static Image getPlaceholder(double w, double h) {
        var placeholder = ImageManager.class.getResource("/media/equipments/placeholder-equipment.png");
        return new Image(placeholder.toExternalForm(), w, h, true, true, true);
    }
}