package ru.dyakov.enumerations;

public enum DepositType {
    WITHDRAW_DEPOSIT(1),
    WITHDRAW_ONLY(2),
    NOTHING(3);

    public final int value;

    private DepositType(int value) {
        this.value = value;
    }
}
