package compiler;

import java.util.HashMap;

public class States{
    public enum State {
        PROCESSING,
        ACCEPTED,
        ERROR,
    }

    public HashMap<State, Boolean> stateRunning = new HashMap<>(){{
        put(State.PROCESSING, true);
        put(State.ACCEPTED, false);
        put(State.ERROR, false);
    }};
}

