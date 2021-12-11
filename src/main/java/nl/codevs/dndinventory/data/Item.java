package nl.codevs.dndinventory.data;

import okhttp3.internal.annotations.EverythingIsNonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.management.InstanceAlreadyExistsException;
import java.util.HashMap;

@EverythingIsNonNull
public class Item implements Comparable<Item> {
    public final ItemType category;
    public final String name;
    public final Money worth;
    @Nullable public final Double weight;
    public final String details;

    /**
     * Get the item name.
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Create a new item.
     * It is usually better to use<br>
     * {@link #makeGetItem(ItemType, String, Money, double, String)}.
     * @param categoryName item category
     * @param itemName item name
     * @param itemWorth item worth {@link Money}
     * @param itemWeight item weight
     * @param itemStats item stats
     */
    Item(
            final ItemType categoryName,
            final String itemName,
            final Money itemWorth,
            @Nullable final Double itemWeight,
            final String itemStats
    ) {
        this.category = categoryName;
        this.name = itemName;
        this.worth = itemWorth;
        this.weight = itemWeight;
        this.details = itemStats;
    }

    /**
     * Create a new or get an existing (exactly equivalent) item.
     * @param categoryName item category
     * @param itemName item name
     * @param itemWorth item worth {@link Money}
     * @param itemWeight item weight
     * @param itemStats item stats
     */
    public static Item makeGetItem(
            final ItemType categoryName,
            final String itemName,
            final Money itemWorth,
            final double itemWeight,
            final String itemStats
    ) {
        return makeGetItem(categoryName, itemName, itemWorth, itemWeight, itemStats, true, true);
    }

    /**
     * Create a new or get an existing (exactly equivalent) item.
     * @param categoryName item category
     * @param itemName item name
     * @param itemWorth item worth {@link Money}
     * @param itemWeight item weight
     * @param itemStats item stats
     * @param saveToDatabase false to not save to database
     * @param checkExists returns an existing item if one exists in the database (recommended)
     */
    public static Item makeGetItem(
            final ItemType categoryName,
            final String itemName,
            final Money itemWorth,
            @Nullable final Double itemWeight,
            final String itemStats,
            final boolean saveToDatabase,
            final boolean checkExists
    ) {
        int hash = hashCode(categoryName, itemName, itemWorth, itemWeight, itemStats);
        if (checkExists && ItemDatabase.get().containsKey(hash)) {
            return ItemDatabase.get().get(hash);
        }
        Item result = new Item(categoryName, itemName, itemWorth, itemWeight, itemStats);
        if (saveToDatabase) {
            try {
                ItemDatabase.add(result);
            } catch (InstanceAlreadyExistsException e) {
                System.out.println("Somehow the item did not exist, but did exist when trying to add it?");
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return name + " (" + category.getName() + ")"
                + " worth " + worth
                + (weight == null ? " no weight" : " weighs " + weight)
                + (details.isEmpty() ? "" : " stats: " + details);
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the {@code hashCode} method
     *     must consistently return the same integer, provided no information
     *     used in {@code equals} comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the {@code equals(Object)}
     *     method, then calling the {@code hashCode} method on each of
     *     the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link Object#equals(Object)}
     *     method, then calling the {@code hashCode} method on each of the
     *     two objects must produce distinct integer results.  However, the
     *     programmer should be aware that producing distinct integer results
     *     for unequal objects may improve the performance of hash tables.
     * </ul>
     *
     * @return a hash code value for this object.
     * @implSpec As far as is reasonably practical, the {@code hashCode} method defined
     * by class {@code Object} returns distinct integers for distinct objects.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return hashCode(category, name, worth, weight, details);
    }

    /**
     * Compute hashcode without instance
     * @param category item category
     * @param name item name
     * @param worth item worth {@link Money}
     * @param weight item weight
     * @param details item details
     * @return hashcode based on aforementioned details
     */
    public static int hashCode(ItemType category, String name, Money worth, @Nullable Double weight, String details) {
        return category.getName().hashCode() + name.hashCode() + worth.hashCode() + ((Double) (weight == null ? 0 : weight)).hashCode() + details.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        return hashCode() == obj.hashCode();
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
    public int compareTo(@NotNull Item o) {
        if (category != o.category) {
            return category.name().compareToIgnoreCase(o.category.name());
        } else {
            return name.compareToIgnoreCase(o.name);
        }
    }
}
