package ostro.veda.common;

import lombok.Getter;

@Getter
@Deprecated
public enum ProcessDataType {

    ADD("ADD"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String type;

    ProcessDataType(String type) {
        this.type = type;
    }

}
