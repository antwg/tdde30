package se.liu.chess.gui;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class handles the loading of images from resources.
 */
public class ImageLoader
{
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private boolean hasFailed = false;

    /**
     * Loads an image from resources and returns it as an ImageIcon and logs
     * potential problems with loading the file.
     * @param name The file name of the image to load.
     * @return The loaded image.
     */
    public ImageIcon loadIMG(final String name) {
	ImageIcon icon = null;
	try {
	    icon = new ImageIcon(findURL(name));
	}
	catch (FileNotFoundException fileNotFoundException) {
	    ImageIcon failedIcon = new ImageIcon("failed", "failed");

	    if (hasFailed) {
		return failedIcon;
	    }

	    hasFailed = true;
	    LOGGER.log(Level.SEVERE, "Could not find: " + name, fileNotFoundException);

	    String message = "Could not load image(s)";
	    String[] options = {"Play without images", "Quit"};

	    int option = JOptionPane.showOptionDialog(null, message, "", JOptionPane.DEFAULT_OPTION,
						      JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

	    if (option == 0){
		return failedIcon;
	    }
	    else{
		System.exit(1);
	    }
	}
	return icon;
    }

    private URL findURL(final String name) throws FileNotFoundException {
	URL url = ClassLoader.getSystemResource("images/" + name + ".png");
	if (url == null) {
	    throw new FileNotFoundException("Could not load file: " + name + ".png");
	}
	else {
	    return url;
	}
    }
}
