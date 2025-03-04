package main.java.ostro.veda.db.helpers.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Action {

    INSERT("INSERT"),
    DELETE("DELETE"),
    UPDATE("UPDATE");

    private final String actionName;
}
