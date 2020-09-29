package com.example.myapplication.tools;

import com.example.myapplication.PlayQuestionsActivity;
import com.example.myapplication.model.CardMode;
import com.example.myapplication.model.CardStatus;

public class CardStatusModifier {
    public static CardStatus updateForMode(CardStatus cardStatus, CardMode cardMode)
    {
        long time = System.currentTimeMillis();
        long currentMinutesUntilNext = cardStatus.getMinutesUntilNextAskDue();
        switch(cardMode)
        {
            case FAIL: currentMinutesUntilNext /= 2;
            break;
            case OK: currentMinutesUntilNext *= 2;
            break;
            case GOOD: currentMinutesUntilNext *=4;
        }
        currentMinutesUntilNext = Math.max(5l, currentMinutesUntilNext);
        return new CardStatus(time, currentMinutesUntilNext);
    }

    public static CardStatus getDefault()
    {
        return new CardStatus(System.currentTimeMillis(), 5);
    }
}
