package za.co.lynx.lift;

/**
 * TODO Document
 */
public interface Lift {

    enum Direction {
        RISING, FALLING
    }

    enum State {
        TRAVELLING, STOPPED
    }

    void addStop(int floorStop);

    void travel();
}
