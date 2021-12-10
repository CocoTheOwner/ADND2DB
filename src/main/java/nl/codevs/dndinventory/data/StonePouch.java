package nl.codevs.dndinventory.data;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.atomic.AtomicBoolean;

public class StonePouch extends ArrayList<StonePouch.Stone> {

    /**
     * Separator for CSV values.
     */
    private static final String CSV_SEPARATOR = ",";

    /**
     * Database file.
     */
    private static final File DATABASE_FILE = new File("stonesdb.csv");

    /**
     * Stone database.
     */
    public static final StonePouch STONE_DATABASE = new StonePouch();

    // Setup database
    static {
        try {
            loadDatabase();
        } catch (FileNotFoundException | InvalidPropertiesFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stones
     * @param name the stone's name
     * @param color the stone's color
     * @param value the stone's {@link Money} value
     * @param details additional details
     */
    public record Stone(
            String name,
            String color,
            Money value,
            String details
    ) {

    }

    /**
     * GSON.
     */
    public static final Gson GSON = new Gson();

    /**
     * Setup database.
     * @throws FileNotFoundException if the database file is missing
     * @throws InvalidPropertiesFormatException if one or more of the lines are faulty
     */
    private static void loadDatabase() throws FileNotFoundException, InvalidPropertiesFormatException {
        AtomicBoolean fail = new AtomicBoolean(false);
        new BufferedReader(new FileReader(DATABASE_FILE)).lines().forEach(line -> {
            String[] elements = line.split(CSV_SEPARATOR);
            if (elements.length < 3) {
                System.out.println("Stone database: " + line + "(" + String.join(",", elements) + ") failed!");
                fail.set(true);
                return;
            }
            String details = elements.length == 4 ? elements[3] : "";
            STONE_DATABASE.add(new Stone(
                    elements[0],
                    elements[1],
                    new Money(Integer.parseInt(elements[2])),
                    details
            ));
        });
        if (fail.get()) {
            throw new InvalidPropertiesFormatException("One or more failed Stone Database entries!");
        }
    }
}
