package io.skysail.server.app.todos;

public class FrequentFlyer {

    private int initialBalance;

    public FrequentFlyer withInitialBalanceOf(int initialBalance) {
        this.initialBalance = initialBalance;
        return this;
    }

    public Object flies(int distance) {
        return null;
    }

    public Integer getBalance() {
        return initialBalance;
    }

}
