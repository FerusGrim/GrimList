package me.ferusgrim.grimlist.database;

public enum Action {
    REMOVED("removed"),
    ADDED("added"),
    INVALID("error"),
    ;

    private final String string;

    Action(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

    public static Action fromString(String string) {
        for (Action action : Action.values()) {
            if (action.toString().equalsIgnoreCase(string)) {
                return action;
            }
        }
        return Action.INVALID;
    }
}
