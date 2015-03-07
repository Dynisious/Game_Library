package GameLibrary.GameObjects;

/**
 * An {@code Object} used to have a {@code Double} value with specific set bounds.
 *
 * @author Dynisious
 */
public final class StatDbl {
    /**
     * A {@code Boolean} value indicating whether the {@code StatDbl} throws errors at
     * values outside the bounds or whether it will set to the closest bound.
     */
    private final boolean autoRegulate;
    /**
     * A {@code double} value representing the minimumVal bound of the {@code StatDbl}'s {@value current#currentVal}.
     */
    private double minimumVal = 0;
    public double minimum;
    public double getMinimum() {
        return minimumVal;
    }
    /**
     * A {@code double} value representing the {@code StatDbl}'s {@value current#currentVal} value.
     */
    private double currentVal = 0;
    public double current;
    public double getCurrent() {
        return currentVal;
    }
    /**
     * Checks the inputed {@code value} before setting {@code currentVal}.
     * 
     * @param value
     * @throws OutOfRangeException 
     */
    public void setCurrent(double value) throws GameLibrary.GameObjects.OutOfRangeException {
        if (value < minimumVal) { //value is less than the minimumVal bound
            if (autoRegulate) { //Set the value to the minimumVal
                currentVal = minimumVal;
            } else { //Throw an error
                throw new OutOfRangeException("\nERROR : Current was set to "
                        + String.format("%.4f which is less than the minimum value of %.4f.", value, minimumVal)); //Throw an exception
            }
        } else if (value > maximumVal) { //value is greater than the maximumVal bound
            if (autoRegulate) { //Set the value to the minimumVal
                currentVal = maximumVal;
            } else { //Throw an error
                throw new OutOfRangeException("\nERROR : Current was set to "
                        + String.format("%.4f which is greater than the maximum value of %.4f.", value, maximumVal)); //Throw an exception
            }
        } else { //value is within the bounds
            currentVal = value; //Set _Current
        }
    }
    /**
     * A {@code double} value representing the maximumVal bound of the {@code StatDbl}'s {@value current#currentVal}.
     */
    private double maximumVal = 0;
    public double maximum;
    public double getMaximum() {
        return maximumVal;
    }

    /**
     * @param min The lower bound of the {@code StatDbl}
     * @param cur The currentVal value of the {@code StatDbl}
     * @param max The upper bound of the {@code StatDbl}
     * @param nAutoRegulate A {@code Boolean} indicating whether the {@code StatDbl}
     *                      handles values outside of the bounds or throws a {@code OutOfRangeException}
     */
    public StatDbl(double min, double cur, double max, boolean nAutoRegulate) {
        minimumVal = min;
        currentVal = cur;
        maximumVal = max;
        autoRegulate = nAutoRegulate;
    }

    /**
     * @param min The new lower bound of the {@code StatDbl}
     * @param max The new upper bound of the {@code StatDbl}
     */
    public void setBounds(double min, double max) { //Changes the maximumVal and minimumVal bounds of the Stat
        minimumVal = min;
        maximumVal = max;
    }

}
