package controller;

import lift.Lift;

/**
 * TODO Document
 */
public class SingleController implements Controller {

    Lift lift;

    public SingleController(Lift lift) {
        this.lift = lift;
    }
}
