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
        if (head == null) {

            head = new Stop(destinationFloor);
            tail = head;

        } else if ((destinationFloor > currentFloor)
                && (destinationFloor < head.floor) ||
                ((destinationFloor < currentFloor)
                        && (destinationFloor > head.floor))) {

            /*
                To join ahead of all positions in the Lift Queue.
                The Stop must be on a floor that would be passed along the way.
             */

            Stop temp = new Stop(destinationFloor);
            temp.next = head;
            head = temp;

        } else if (((destinationFloor < head.floor)
                && (destinationFloor < tail.floor)) ||
                ((destinationFloor > head.floor) && (destinationFloor > tail.floor))) {

            /*
                If the destination is not inside the range
                between the first floor and last floor in the Queue,
                it is added to the end.
             */
            Stop temp = new Stop(destinationFloor);
            tail.next = temp;
            tail = temp;
        } else {

            /*
                If none of the above conditions are met,
                the destinationFloor is to be inserted
                somewhere inside the Queue.
             */
            Stop temp = head;
            boolean increasing = (temp.floor < temp.next.floor);
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
    }

    /**
     * @return true if the Queue has another Stop.
     */
    public boolean hasNext() {
        return (head != null);
    }
}
