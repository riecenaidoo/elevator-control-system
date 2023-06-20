package za.co.lynx.lift;

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
    @Override
    public void addStop(int floorStop) {
        if (this.floor == floorStop)
            throw new RuntimeException("[BAD] Lift should not be instructed to travel to a floor it is already on.");

        queue.addStop(this.floor, floorStop);
    }

    /**
     * Traverses the Lift's Queue of Stops.
     */
    @Override
    public void travel() {
        new Thread(() -> {
            while (queue.hasNext()) {
                int nextStop = queue.popNextStop();
                direction = (nextStop > floor) ? Direction.RISING : Direction.FALLING;
                int step = (direction.equals(Direction.RISING)) ? 1 : -1;
                state = State.TRAVELLING;

                while (state.equals(State.TRAVELLING)) {
                    try {
                        Thread.sleep(500);  // Simulate Travel Time.
                        floor += step;
                    } catch (InterruptedException e) {
                        throw new RuntimeException("[BAD] Lift thread was interrupted during travel time.");
                    }

                    state = (floor != nextStop) ? State.TRAVELLING : State.STOPPED;
                    switch (state) {
                        case TRAVELLING -> System.out.printf("[Lift] Visited floor %d.\n", floor);
                        case STOPPED -> System.out.printf("[Lift] Arrived at floor %d.\n", nextStop);
                    }
                }
            }
        }).start();
    }
}
