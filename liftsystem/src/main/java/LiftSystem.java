import controller.Controller;
import controller.SingleController;
import lift.BasicLift;

import java.util.Scanner;

/**
 * Demo of the Lift System.
 */
public class LiftSystem {

    static int lowestFloor = 1;
    static int highestFloor = 10;

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Controller controlUnit = new SingleController(new BasicLift());

        controlUnit.call(getFloorNumber());
    }

    /**
     * @return floor number between the lowestFloor and highestFloor.
     */
    protected static int getFloorNumber() {
        int floor = 0;
        boolean valid = false;

        do {
            try {
                System.out.println("(Floor Num?) > ");
                floor = Integer.parseInt(getInput());
            } catch (NumberFormatException e) {
                System.out.println("[HELP] Please input a number only.");
                continue;
            }

            valid = ((floor >= lowestFloor)
                    && (floor <= highestFloor));

            if (!valid) {
                System.out.printf("[HELP] Please select a Floor between %d-%d.",
                        lowestFloor, highestFloor);
            }
        } while (!valid);

        return floor;
    }

    /**
     * @return non-empty user input from the terminal.
     */
    protected static String getInput() {
        String input;

        do {
            input = sc.nextLine();
        } while (input.isBlank());

        return input;
    }
}
