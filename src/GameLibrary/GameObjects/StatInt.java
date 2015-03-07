package GameLibrary.GameObjects;

/**
 * An {@code Object} used to have a {@code Integer} value with specific set
 * bounds.
 *
 * @author Dynisious
 */
public final class StatInt {

    /**
     * A {@code Boolean} value indicating whether the {@code StatInt} throws
     * errors at values outside the bounds or whether it will set to the closest
     * bound.
     */
    private final boolean autoRegulate;
    /**
     * A {@code Integer} value representing the minimumVal bound of the
     * {@code StatInt}'s {@value current#currentVal}.
     */
    private int minimumVal = 0;
    public int minimum;
    /**
     * A {@code Integer} value representing the {@code StatInt}'s
     * {@value current#currentVal} value.
     *
     * @return Returns {@value current#currentVal}.
     */
    public int getMinimum() {
        return minimumVal;
    }
    /**
     * A {@code Integer} value representing the {@code StatInt}'s
     * {@value current#currentVal} value.
     */
    private int currentVal = 0;
    public int current;
    public int getCurrent() {
        return currentVal;
    }
    /**
     * Checks the inputed {@code value} before setting {@code currentVal}.
     *
     * @param value
     * @throws OutOfRangeException
     */
    public void SetCurrent(int value) throws OutOfRangeException {
        if (value < minimumVal) { //value is less than the minimumVal bound
            if (autoRegulate) { //Automatically regulate the stat
                currentVal = minimumVal; //Set the value to minimumVal
            } else { //Throw an error
                throw new OutOfRangeException("\nERROR : Current was set to "
                        + String.format("%d which is less than the minimum value of %d.", value, minimumVal)); //Throw an exception
            }
        } else if (value > maximumVal) { //value is greater than the maximumVal bound
            if (autoRegulate) { //Automatically regulate the stat
                currentVal = maximumVal; //Set the value to maximumVal
            } else { //Throw an error
                throw new OutOfRangeException("\nERROR : Current was set to "
                        + String.format("%d which is greater than the maximum value of %d.", value, maximumVal)); //Throw an exception
            }
        } else { //value is within the bounds
            currentVal = value; //Set _Current
        }
    }
    /**
     * A {@code Integer} value representing the maximumVal bound of the
     * {@code StatInt}'s {@value current#currentVal}.
     */
    private int maximumVal = 0;
    public int maximum;
    public int getMaximum() {
        return maximumVal;
    }

    /**
     * @param min The lower bound of the {@code StatInt}
     * @param cur The currentVal value of the {@code StatInt}
     * @param max The upper bound of the {@code StatInt}
     * @param nAutoRegulate A {@code Boolean} indicating whether the
     * {@code StatInt} handles values outside of the bounds or throws a
     * {@code OutOfRangeException}
     */
    public StatInt(int min, int cur, int max, boolean nAutoRegulate) {
        minimumVal = min;
        currentVal = cur;
        maximumVal = max;
        autoRegulate = nAutoRegulate;
    }

    /**
     * @param min The new lower bound of the {@code StatInt}
     * @param max The new upper bound of the {@code StatInt}
     */
    public void setBounds(int min, int max) {
        minimumVal = min;
        maximumVal = max;
    }

}
