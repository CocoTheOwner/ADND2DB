package nl.codevs.dndinventory.data;

import nl.codevs.strinput.system.Param;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public class StonePouch extends ArrayList<StonePouch.Stone> {

    /**
     * Separator for CSV values.
     */
    private static final String CSV_SEPARATOR = ",";

    /**
     * Database file.
     */
    private static final File DATABASE_FILE = new File("DNDInventories/databases/stonesdb.csv");

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
     * Stones.
     * Value must is in GP only!
     */
    public static class Stone implements Comparable<Stone> {
        final String name;
        final String color;
        final int valueGP;
        final String details;

        /**
         * Create a stone.
         * @param name the stone's name
         * @param color the stone's color
         * @param valueGP the stone's GP value
         * @param details additional details
         */
        public Stone(
                final String name,
                final String color,
                final int valueGP,
                final String details
        ) {
            this.name = name;
            this.color = color;
            this.valueGP = valueGP;
            this.details = details;
        }

        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         * <p>The implementor must ensure
         * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
         * for all {@code x} and {@code y}.  (This
         * implies that {@code x.compareTo(y)} must throw an exception iff
         * {@code y.compareTo(x)} throws an exception.)
         *
         * <p>The implementor must also ensure that the relation is transitive:
         * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
         * {@code x.compareTo(z) > 0}.
         *
         * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
         * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
         * all {@code z}.
         *
         * <p>It is strongly recommended, but <i>not</i> strictly required that
         * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
         * class that implements the {@code Comparable} interface and violates
         * this condition should clearly indicate this fact.  The recommended
         * language is "Note: this class has a natural ordering that is
         * inconsistent with equals."
         *
         * <p>In the foregoing description, the notation
         * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
         * <i>signum</i> function, which is defined to return one of {@code -1},
         * {@code 0}, or {@code 1} according to whether the value of
         * <i>expression</i> is negative, zero, or positive, respectively.
         *
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it
         *                              from being compared to this object.
         */
        @Override
        public int compareTo(@NotNull StonePouch.Stone o) {
            if (!name.equalsIgnoreCase(o.name)) {
                return name.compareToIgnoreCase(o.name);
            } else {
                return Integer.compare(valueGP, o.valueGP);
            }
        }

        /**
         * Returns a string representation of the object. In general, the
         * {@code toString} method returns a string that
         * "textually represents" this object. The result should
         * be a concise but informative representation that is easy for a
         * person to read.
         * It is recommended that all subclasses override this method.
         * <p>
         * The {@code toString} method for class {@code Object}
         * returns a string consisting of the name of the class of which the
         * object is an instance, the at-sign character `{@code @}', and
         * the unsigned hexadecimal representation of the hash code of the
         * object. In other words, this method returns a string equal to the
         * value of:
         * <blockquote>
         * <pre>
         * getClass().getName() + '@' + Integer.toHexString(hashCode())
         * </pre></blockquote>
         *
         * @return a string representation of the object.
         */
        @Override
        public String toString() {
            return name + "," + color + "," + valueGP + "," + details;
        }
    }

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
                    Integer.parseInt(elements[2]),
                    details
            ));
        });
        if (fail.get()) {
            throw new InvalidPropertiesFormatException("One or more failed Stone Database entries!");
        }
    }
}
