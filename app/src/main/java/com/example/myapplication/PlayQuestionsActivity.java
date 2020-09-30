package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.CardStatus;
import com.example.myapplication.model.FlashCard;
import com.example.myapplication.model.InputType;
import com.example.myapplication.tools.CardResourceStore;

import java.util.List;
import java.util.Locale;

public class PlayQuestionsActivity extends AppCompatActivity {

    TextToSpeech tts;
    private static final int SPEECH_REQUEST_CODE = 0;
    private static final int secondsWaitForAnswer = 20;
    private FlashCard currentFlashcard;
    List<FlashCard> flashCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_questions);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });
        flashCards = CardResourceStore.getFlashCards(getSharedPreferences(CardResourceStore.getCardFileName(), Context.MODE_PRIVATE));
    }

    /**
     * Get the most urgent flashcard based on time til next due, if no card due within next 5 mins returns null
     *
     * @return most urgent card
     */
    private FlashCard getMostUrgentFlashcard() {
        long currentTimeMillis = System.currentTimeMillis();
        long currentLowestTimeDue = 0l;
        FlashCard currentMostUrgentCard = null;
        for (FlashCard card : flashCards) {
            CardStatus cardStatus = card.getCardStatus();
            long timeNextDue = cardStatus.getUnixTimeLastAsked() + (cardStatus.getMinutesUntilNextAskDue() * 60 * 1000);
            if (currentLowestTimeDue == 0l || timeNextDue < currentLowestTimeDue) {
                currentLowestTimeDue = timeNextDue;
                currentMostUrgentCard = card;
            }
        }
        return (currentTimeMillis + (5 * 60 * 1000)) >= currentLowestTimeDue ? currentMostUrgentCard : null;
    }

    public void onClickStartButton(View view) {
//        Toast.makeText(this, "Speaking!", Toast.LENGTH_SHORT).show();
//
//        displaySpeechRecognizer();
        tts.speak("Starting new learning session in 3 2 1", TextToSpeech.QUEUE_FLUSH, null);
    }

    private void askMostUrgentQuestion() {
        while (tts.isSpeaking()) {
            //wait?
        }
        final FlashCard flashCard = getMostUrgentFlashcard();
        currentFlashcard = flashCard;
        tts.speak(flashCard.getQuestion(), TextToSpeech.QUEUE_ADD, null);
        while (tts.isSpeaking()) {
            //wait?
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speak("Answer", TextToSpeech.QUEUE_ADD, null);
                tts.speak(flashCard.getAnswer(), TextToSpeech.QUEUE_ADD, null);
                while (tts.isSpeaking()) {
                    //wait?
                }
                displaySpeechRecognizer(InputType.CARD_MODE);
            }
        }, secondsWaitForAnswer * 1000);
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer(InputType inputType) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra("inputType", inputType.name());
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            if (data.hasExtra("inputType")) {
                InputType inputType = InputType.valueOf(data.getStringExtra("inputType"));
                if (inputType == InputType.CARD_MODE) {
                    tts.speak("Card rated " + spokenText, TextToSpeech.QUEUE_ADD, null);
                    CardStatus cardStatus = currentFlashcard.getCardStatus();
                    long lastMinutesUntilNextAsk = cardStatus.getMinutesUntilNextAskDue();
                    if (spokenText.toLowerCase().equals("fail")) {
                        lastMinutesUntilNextAsk = 5l;
                    } else if (spokenText.toLowerCase().equals("ok")) {
                        lastMinutesUntilNextAsk += 30;
                    } else if (spokenText.toLowerCase().equals("good")) {
                        lastMinutesUntilNextAsk = Math.max(lastMinutesUntilNextAsk * 3, 23 * 60);
                    }
                    flashCards.remove(currentFlashcard);
                    CardStatus newStatus = new CardStatus(cardStatus.getUnixTimeLastAsked(), lastMinutesUntilNextAsk);
                    FlashCard updatedFlashcard = new FlashCard(currentFlashcard.getQuestion(), currentFlashcard.getAnswer(), currentFlashcard.getTopic(), currentFlashcard.getUuid(), newStatus);
                    CardResourceStore.saveFlashCard(updatedFlashcard, getSharedPreferences(CardResourceStore.getCardFileName(), Context.MODE_PRIVATE));
                }
            }
            // Do something with spokenText
        }
        if(flashCards.size()>0)
        {
            askMostUrgentQuestion();
        }
    }
}