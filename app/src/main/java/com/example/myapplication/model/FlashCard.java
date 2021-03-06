package com.example.myapplication.model;

import com.example.myapplication.tools.CardStatusModifier;

import java.util.UUID;

public class FlashCard {
    private final String topic;
    private final String question;
    private final String answer;
    private final String uuid;
    private CardStatus cardStatus;

    public FlashCard(String question, String answer, String topic) {
        this.question = question;
        this.answer = answer;
        this.topic = topic;
        this.uuid = UUID.randomUUID().toString().replace("-", "");
        cardStatus = CardStatusModifier.getDefault();
    }

    public FlashCard(String question, String answer, String topic, String uuid, CardStatus cardStatus) {
        this.question = question;
        this.answer = answer;
        this.topic = topic;
        this.uuid = uuid;
        this.cardStatus = CardStatus.copyOf(cardStatus);
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTopic() {
        return topic;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }
}
