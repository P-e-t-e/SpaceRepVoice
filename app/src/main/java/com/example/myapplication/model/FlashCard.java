package com.example.myapplication.model;

import java.util.UUID;

public class FlashCard {
    private final String topic;
    private final String question;
    private final String answer;
    private final String uuid;

    public FlashCard(String question, String answer, String topic) {
        this.question = question;
        this.answer = answer;
        this.topic = topic;
        this.uuid = UUID.randomUUID().toString().replace("-", "");
    }

    public FlashCard(String question, String answer, String topic, String uuid) {
        this.question = question;
        this.answer = answer;
        this.topic = topic;
        this.uuid = uuid;
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
}
