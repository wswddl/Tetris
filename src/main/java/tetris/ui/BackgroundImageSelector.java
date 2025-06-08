package tetris.ui;

import javafx.scene.Node;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageSelector {
    private final List<String> backgroundImagesFileNames = new ArrayList<>(); // stores all the file names in the directory
    private final String backgroundImagesDirectory;
    private String currentChosenImageFileName;

    public ImageSelector(String imageDirectoryPath) {
        backgroundImagesDirectory = imageDirectoryPath;
        scanForImagesInDirectory(backgroundImagesDirectory);
    }
    private void scanForImagesInDirectory(String imageDirectoryPath) {
        try {
            URL dirUrl = getClass().getResource(imageDirectoryPath);
            String decodedPath = URLDecoder.decode(dirUrl.getFile(), "UTF-8");
            File directory = new File(decodedPath);

            // Note: listFiles applies filter (the lambda block) and lists all the files in the directory
            File[] filesInDirectory = directory.listFiles();

            if (filesInDirectory == null || filesInDirectory.length == 0) {
                System.err.println("No image files found in directory: " + imageDirectoryPath);
                return;
            }

            // Add the file names in files into the array list
            for (File file : filesInDirectory) {
                backgroundImagesFileNames.add(file.getName());

            }
        } catch (Exception e) {
            System.err.println("Error loading image names: " + e.getMessage());
        }


        //File directory = new File(dirUrl.getFile());
/*
        if (!directory.exists() || !directory.isDirectory()) {
            //System.err.println("Error: Directory not found or is not a directory: " + imageDirectoryPath);
            return;
        }*/

    }

    /**
     * Choose a random image file name in {@code backgroundImagesFileNames} and set the background for the given node
     * via css set style method.
     * This method ensures no image will be selected twice in a row.
     * @param mainNode is the node to which the background will be applied
     */
    public void setRandomBackgroundImage(Node mainNode) {

        if (backgroundImagesFileNames.isEmpty()) {
            System.err.println("No background images available. Cannot set a random background.");
            return;
        }
        // Ensures no image will be selected twice in a row
        String imagePath = chooseDifferentBackgroundImage();
        // set the background in the node (For GameScreen, it is "mainLayout")
        mainNode.setStyle(
                "-fx-background-image: url('" + imagePath + "');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center;" +
                        "-fx-background-repeat: no-repeat;"
        );
    }

    private String chooseDifferentBackgroundImage() {
        String chosenImageFileName;
        do {
            Random random = new Random();
            int randomImageIndex = random.nextInt(backgroundImagesFileNames.size());
            System.out.println(randomImageIndex);
            chosenImageFileName = backgroundImagesFileNames.get(randomImageIndex);
        } while (chosenImageFileName.equals(currentChosenImageFileName));

        currentChosenImageFileName = chosenImageFileName;
        // Set the background will the chosen image
        String imagePath = backgroundImagesDirectory + chosenImageFileName;
        return imagePath;
    }
}
