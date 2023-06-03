package lift;

/**
 * Represents a FIFO* style Queue system for a Lift that
 * priorities moving the Lift in one direction
 * for as long as possible.
 * <br><br>
 * <i>* It is not exactly first in first out, as is supports
 * insertion into the middle of the Queue, provided the insertion
 * would keep the Lift traversing in one direction.</i>
 */
public class LiftQueue {

    Stop head;
    Stop tail;
    Stop pivot;

    static class Stop {
        int floor;
        Stop next;

        Stop(int floor) {
            this.floor = floor;
            next = null;
        }
    }

    public LiftQueue() {
        head = null;
        tail = null;
    }

    /**
     * @return true if the Queue has another Stop.
     */
    public boolean hasNext() {
        return (head != null);
    }

    /**
     * Pops the first Stop out the Queue and
     * retrieves the floor number associated with it.
     *
     * @return the first Stop in the Queue's floor.
     */
    public int popNextStop() {

        if (head == null) throw new RuntimeException("[BAD] Popping empty Queue.");

        int floor = head.floor;
        head = head.next;

        /*
            Garbage collection will remove the previous head
            as there is  no longer any reference to it.
         */

        return floor;
    }

    /**
     * Adds a Stop to the Queue.
     *
     * @param currentFloor     that the Lift is currently at.
     * @param destinationFloor of the Stop to be added.
     */
    public void addStop(int currentFloor, int destinationFloor) {

        switch (getInsertionStyle(currentFloor, destinationFloor)) {
            case CREATE -> {
                Stop temp = new Stop(destinationFloor);
                head = temp;
                tail = temp;
                pivot = temp;
            }
            case ENQUEUE -> {
                Stop temp = new Stop(destinationFloor);
                tail.next = temp;
                if (pivot == tail) pivot = temp;
                tail = temp;
            }
            case INSERT -> {

                boolean increasing = (head.floor < head.next.floor);

                Stop temp;
                // Determine which section of the Queue to insert in.
                if ((increasing && (destinationFloor < tail.floor)) ||
                        (!increasing && (destinationFloor > tail.floor))) {
                    temp = head;
                } else {
                    temp = pivot;
                    increasing = !increasing;   // The pivot is going in the opposite direction
                }

                while (temp.next != null) {
                    if ((increasing && temp.floor < destinationFloor
                            && destinationFloor < temp.next.floor) ||
                            (!increasing && temp.floor > destinationFloor && destinationFloor > temp.next.floor)) {
                        {
                            Stop insert = new Stop(destinationFloor);
                            insert.next = temp.next;
                            temp.next = insert;
                        }
                    }
                    temp = temp.next;   // Traverse
                }
            }
            case JUMP -> {
                Stop temp = new Stop(destinationFloor);
                temp.next = head;
                head = temp;
            }
            case PIVOT -> {
                Stop temp = new Stop(destinationFloor);
                tail.next = temp;
                tail = temp;
            }
        }
    }

    /**
     * To join ahead of all positions in the Lift Queue.
     * The Stop must be on a floor that would be passed along the way.
     *
     * @param currentFloor     that the Lift is currently at.
     * @param destinationFloor of the Stop to be added.
     * @return true if the Stop should join at the head of the queue.
     */
    private boolean isJump(int currentFloor, int destinationFloor) {
        return (destinationFloor > currentFloor)
                && (destinationFloor < head.floor) ||
                ((destinationFloor < currentFloor)
                        && (destinationFloor > head.floor));
    }

    /**
     * If the destination is not inside the range
     * between the first floor and last floor in the Queue,
     * it is added to the end.
     *
     * @param destinationFloor of the Stop to be added.
     * @return true if the Stop should join at the tail of the queue.
     */
    private boolean isEnqueue(int destinationFloor) {
        return ((destinationFloor < head.floor)
                && (destinationFloor < tail.floor)) ||
                ((destinationFloor > head.floor) && (destinationFloor > tail.floor));
    }

    private boolean isPivot(int destinationFloor) {

        if (head.next == null) return false;

        boolean increasing = (head.floor < head.next.floor);

        return (increasing && (destinationFloor < tail.floor)) ||
                (!increasing && (destinationFloor > tail.floor));
    }

    private InsertionStyle getInsertionStyle(int currentFloor, int destinationFloor) {
        if (head == null) return InsertionStyle.CREATE;

        if (isJump(currentFloor, destinationFloor)) return InsertionStyle.JUMP;

        if (isEnqueue(destinationFloor)) {
            if (isPivot(destinationFloor)) return InsertionStyle.PIVOT;

            return InsertionStyle.ENQUEUE;
        }

        /*
            If none of the above conditions are met,
            the destinationFloor is to be inserted
            somewhere inside the Queue.
         */
        return InsertionStyle.INSERT;
    }

    enum InsertionStyle {
        CREATE, ENQUEUE, INSERT, JUMP, PIVOT
    }
}
