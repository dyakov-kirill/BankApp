package ru.dyakov.enumerations;

public enum StatusTypes {
    IN_PROGRESS(1),
    ACCEPTED(2),
    APPROVED(3),
    REJECTED(4);

    public final int id;
    StatusTypes(int id) {
        this.id = id;
    }
}
