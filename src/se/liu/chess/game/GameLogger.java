package se.liu.chess.game;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger
{

    public static void setUpLogger() {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	try {
	    logger.setLevel(Level.ALL);
	    FileHandler fileText = new FileHandler("Logging.txt");

	    SimpleFormatter formatterText = new SimpleFormatter();
	    fileText.setFormatter(formatterText);
	    logger.addHandler(fileText);

	} catch (IOException e) {
	    // (Inspection) Can't log if log failed
	    String[] options = {"Yes", "No"};
	    int choice = JOptionPane.showOptionDialog(null, "Could not access Logging.txt, play anyway?", "",
						      JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
	    if (choice == 1) {
		System.exit(1);
	    }
	    e.printStackTrace();
	}
    }
}
