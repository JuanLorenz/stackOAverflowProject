package utilities.manager;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class ImageManager {

    private static final String BASE_RES = "src/main/resources/";
    public static final String TYPE_EQUIPMENT = "media/equipments/";
    public static final String TYPE_PROFILE = "media/profiles/";

    /**
     * Centralized FileChooser logic
     */
    public static File chooseImage(Button triggerButton) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        return fileChooser.showOpenDialog(triggerButton.getScene().getWindow());
    }

    /**
     * Centralized Save logic.
     * Handles both Equipment and Profiles.
     */
    public static String saveImage(File file, String type) {
        if (file == null) {
            return type.equals(TYPE_EQUIPMENT) ? TYPE_EQUIPMENT + "placeholder-equipment.png"
                    : TYPE_PROFILE + "placeholder-profile.png";
        }

        try {
            Path dir = Paths.get(BASE_RES + type);
            if (!Files.exists(dir)) Files.createDirectories(dir);

            String fileName = System.currentTimeMillis() + "_" + file.getName();
            Path target = dir.resolve(fileName);

            Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            return type + fileName; // Returns the DB-ready path
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Centralized Update logic.
     * Deletes the old file and saves the new one.
     */
    public static String updateImage(String oldPath, File newFile, String type) {
        // 1. Delete the old file (if it's not a placeholder)
        deleteImage(oldPath);
        // 2. Save the new file
        return saveImage(newFile, type);
    }

    /**
     * Centralized Delete logic.
     * Prevents deleting the placeholders.
     */
    public static boolean deleteImage(String imagePath) {
        if (imagePath == null || imagePath.contains("placeholder")) return false;

        try {
            // Remove leading slash if present
            String cleanPath = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;
            Path filePath = Paths.get(BASE_RES + cleanPath);

            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Centralized Retrieval logic.
     * Returns a valid Image or the correct placeholder based on path context.
     */
    public static Image getSafeImage(String path, double width, double height) {
        // Default to Equipment placeholder unless path contains 'profiles'
        String placeholder = (path != null && path.contains("profiles"))
                ? "/" + TYPE_PROFILE + "placeholder-profile.png"
                : "/" + TYPE_EQUIPMENT + "placeholder-equipment.png";

        if (path == null || path.isBlank()) {
            return loadFromResource(placeholder, width, height);
        }

        try {
            File file = new File(BASE_RES + path);
            if (file.exists()) {
                // Loading with 'true' for background loading to keep UI smooth
                return new Image(file.toURI().toString(), width, height, true, true, true);
            }
        } catch (Exception e) {
            System.err.println("Error loading file from disk: " + path);
        }

        // Ensure path starts with / for getResource
        String formattedPath = path.startsWith("/") ? path : "/" + path;
        var resource = ImageManager.class.getResource(formattedPath);

        if (resource == null) {
            System.err.println("IMAGE NOT FOUND: " + formattedPath);
            return loadFromResource(placeholder, width, height);
        }

        return new Image(resource.toExternalForm(), width, height, true, true, true);
    }

    private static Image loadFromResource(String path, double w, double h) {
        var res = ImageManager.class.getResource(path);
        if (res == null) return null; // Total failure
        return new Image(res.toExternalForm(), w, h, true, true, true);
    }
}