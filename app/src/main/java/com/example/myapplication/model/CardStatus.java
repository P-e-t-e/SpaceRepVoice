package com.example.myapplication.model;

public class CardStatus {
    private final long unixTimeLastAsked;
    private final long minutesUntilNextAskDue;

    public CardStatus(long unixTimeLastAsked, long minutesUntilNextAskDue) {
        this.unixTimeLastAsked = unixTimeLastAsked;
        this.minutesUntilNextAskDue = minutesUntilNextAskDue;
    }

    public long getUnixTimeLastAsked() {
        return unixTimeLastAsked;
    }

    public long getMinutesUntilNextAskDue() {
        return minutesUntilNextAskDue;
    }

    public static CardStatus copyOf(CardStatus cardStatus) {
        return new CardStatus(cardStatus.getUnixTimeLastAsked(), cardStatus.minutesUntilNextAskDue);
    }
}
