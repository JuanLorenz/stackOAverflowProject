package utilities.manager;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageManager {

    private static final String EQUIPMENT_DIR = "src/main/resources/media/equipments/";

    public static File chooseImage(Button triggerButton) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        return fileChooser.showOpenDialog(triggerButton.getScene().getWindow());
    }

    // 2. Logic to SAVE the file
    public static String saveEquipmentImage(File file) {
        if (file == null) return "media/equipments/placeholder-equipment.png";

        try {
            Path dir = Paths.get(EQUIPMENT_DIR);
            if (!Files.exists(dir)) Files.createDirectories(dir);

            String fileName = System.currentTimeMillis() + "_" + file.getName();
            Path target = dir.resolve(fileName);

            Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            return "media/images/equipmentImages/" + fileName;
        } catch (IOException e) {
            System.out.println("Failed to save file " + file.getName());
            e.printStackTrace();
            return null;
        }
    }

    public static String saveImage(File sourceFile) {

        try {

            File directory = new File(EQUIPMENT_DIR);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + sourceFile.getName();

            Path targetPath = Paths.get(EQUIPMENT_DIR + uniqueFileName);

            Files.copy(
                    sourceFile.toPath(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "media/profiles/" + uniqueFileName;

        } catch (IOException e) {

            e.printStackTrace();

            return "/media/profiles/placeholder-profile.png";
        }
    }

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
            return getPlaceholderEquipment(width, height);
        }

        // 2. Check if the file actually exists in your resources
        // getClass().getResource() returns null if the path is invalid
        var resource = ImageManager.class.getResource(path);

        if (resource == null) {
            System.err.println("IMAGE NOT FOUND: " + path);
            return getPlaceholderEquipment(width, height);
        }

        // 3. Load the image now that we know the URL is safe
        // (Using the 6-parameter constructor for performance)
        return new Image(resource.toExternalForm(), width, height, true, true, true);
    }

    private static Image getPlaceholderEquipment(double w, double h) {
        var placeholder = ImageManager.class.getResource("/media/equipments/placeholder-equipment.png");
        return new Image(placeholder.toExternalForm(), w, h, true, true, true);
    }
}