package me.ferusgrim.grimlist.database;

public enum DatabaseType {
    HOCON("hocon"),
    MYSQL("mysql"),
    ;

    private final String string;

    DatabaseType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

    public static DatabaseType fromString(String string) {
        for (DatabaseType type : DatabaseType.values()) {
            if (type.toString().equalsIgnoreCase(string)) {
                return type;
            }
        }
        return DatabaseType.HOCON;
    }
}
