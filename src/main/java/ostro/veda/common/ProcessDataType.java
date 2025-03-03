package ostro.veda.common;

@Deprecated
public enum ProcessDataType {

    ADD("ADD"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String type;

    ProcessDataType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
