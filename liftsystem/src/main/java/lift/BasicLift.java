package lift;

/**
 * A basic Lift, without additional real-world properties (such as velocity, braking, etc.),
 * to simulate the basic principles of a Lift; an object that visits floors in its queue.
 */
public class BasicLift implements Lift {

    Direction direction;
    int floor;
    LiftQueue queue;
    State state;

    public BasicLift() {
        floor = 0;
        direction = Direction.RISING;
        queue = new LiftQueue();
        state = State.STOPPED;
    }

    /**
     * Adds another stop to the Lift's queue.
     *
     * @param floorStop The floor number to stop at.
     */
    public void addStop(int floorStop) {
        if (this.floor == floorStop)
            throw new RuntimeException("[BAD] Lift should not be instructed to travel to a floor it is already on.");

        queue.addStop(this.floor, floorStop);
    }

    /**
     * Travels to the next stop in the Lift's queue.
     */
    public void travel() {
        int nextStop = queue.popNextStop();
        direction = (nextStop > floor) ? Direction.RISING : Direction.FALLING;
        int step = (direction.equals(Direction.RISING)) ? 1 : -1;
        state = State.TRAVELLING;

        new Thread(() -> {
            for (int i = floor; i < nextStop; i += step) {
                try {
                    Thread.sleep(500);  // Simulate Travel Time.
                } catch (InterruptedException e) {
                    throw new RuntimeException("[BAD] Lift thread was interrupted during travel time.");
                }
                System.out.printf("[Lift] Visited floor %d.\n", i);
            }

            System.out.printf("[Lift] Arrived at floor %d.\n", nextStop);
            state = State.STOPPED;
        }).start();
    }
}
