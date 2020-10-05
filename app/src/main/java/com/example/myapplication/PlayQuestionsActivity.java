package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;

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
        tts.speak("Starting new learning session in 3 2 1", TextToSpeech.QUEUE_FLUSH, null);
    }

    private void askMostUrgentQuestion(boolean repeat) {
        //Get the most urgent flashcard or finish and close if all finished
        if(!repeat)
        {
            setCurrentUrgentFlashcard();
        }

        //flush tts queue because we are are back at the start of the process
        //Ask the question
        tts.speak(currentFlashcard.getQuestion(), TextToSpeech.QUEUE_FLUSH, null);

        //Speak the answer 20-30 seconds after question TTS finished
        Runnable speakAnswer = new Runnable() {
            @Override
            public void run() {
                speakAnswerAndGetInput(currentFlashcard, secondsWaitForAnswer, tts);
            }
        };
        doSomethingOnceTTSIsFinishedSpeaking(tts, speakAnswer, 1);
    }

    private void setCurrentUrgentFlashcard() {
        final FlashCard flashCard = getMostUrgentFlashcard();

        //If no cards are left, lets return to main activity and display a toast
        if(flashCard == null)
        {

            Intent intent = new Intent();
            intent.putExtra("toast", "All cards finished - good job!");
            setResult(RESULT_OK, intent);
            finish();
        }

        //Otherwise lets proceed with the question
        this.currentFlashcard = flashCard;
    }

    /**
     * Speak the answer after agreed delay, then get input from user once answer tts
     * has finished speaking
     * @param flashCard
     */
    private void speakAnswerAndGetInput(final FlashCard flashCard, int delay, final TextToSpeech tts) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speak("Answer", TextToSpeech.QUEUE_ADD, null);
                tts.speak(flashCard.getAnswer(), TextToSpeech.QUEUE_ADD, null);
                //once answer TTS is finished, ask for user input within 10 seconds
                doSomethingOnceTTSIsFinishedSpeaking(tts, getDefaultDisplaySpeechRegignizerRunnable(InputType.CARD_MODE), 1);
            }
        }, delay * 1000);
    }


    public void doSomethingOnceTTSIsFinishedSpeaking(final TextToSpeech tts, final Runnable action, final int backoffSeconds)
    {
        if(tts.isSpeaking())
        {
            Runnable retry = new Runnable() {
                @Override
                public void run() {
                    doSomethingOnceTTSIsFinishedSpeaking(tts, action, backoffSeconds * 2);
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(retry, 1000 * backoffSeconds);
        }
        else
        {
            action.run();
        }
    }

    private Runnable getDefaultDisplaySpeechRegignizerRunnable(final InputType inputType)
    {
        return new Runnable() {

            @Override
            public void run() {
                displaySpeechRecognizer(inputType);
            }
        };
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
                    onCardModeSpeechInput(spokenText);
                }
            }
        }
    }

    private void onCardModeSpeechInput(String spokenText) {
        tts.speak("You said" + spokenText, TextToSpeech.QUEUE_ADD, null);
        CardStatus cardStatus = currentFlashcard.getCardStatus();
        long lastMinutesUntilNextAsk = cardStatus.getMinutesUntilNextAskDue();
        if (spokenText.toLowerCase().equals("fail")) {
            lastMinutesUntilNextAsk = 5l;
        } else if (spokenText.toLowerCase().equals("ok")) {
            lastMinutesUntilNextAsk += 30;
        } else if (spokenText.toLowerCase().equals("good")) {
            lastMinutesUntilNextAsk = Math.max(lastMinutesUntilNextAsk * 3, 23 * 60);
        } else if (spokenText.toLowerCase().equals("repeat"))
        {
            //we want to repeat the card, so just start process again without updating currentCard
            doSomethingOnceTTSIsFinishedSpeaking(tts, new Runnable() {
                @Override
                public void run() {
                    askMostUrgentQuestion(true);
                }
            }, 1);
            return;
        }
        flashCards.remove(currentFlashcard);
        CardStatus newStatus = new CardStatus(cardStatus.getUnixTimeLastAsked(), lastMinutesUntilNextAsk);
        FlashCard updatedFlashcard = new FlashCard(currentFlashcard.getQuestion(), currentFlashcard.getAnswer(), currentFlashcard.getTopic(), currentFlashcard.getUuid(), newStatus);
        CardResourceStore.saveFlashCard(updatedFlashcard, getSharedPreferences(CardResourceStore.getCardFileName(), Context.MODE_PRIVATE));
        //move onto next question
        if(flashCards.size()>0)
        {
            askMostUrgentQuestion(false);
        }
    }
}