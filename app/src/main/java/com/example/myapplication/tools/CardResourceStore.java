package com.example.myapplication.tools;

import android.content.SharedPreferences;

import com.example.myapplication.model.FlashCard;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardResourceStore {
    public static final String CARD_FILE_NAME = "flash_card_resource";

    public static void saveFlashCard(FlashCard flashCard, SharedPreferences sharedPreferences) {
        Gson gson = new Gson();
        String serializedCard = gson.toJson(flashCard);
        sharedPreferences.edit().putString(flashCard.getUuid(), serializedCard).apply();
    }

    public static List<FlashCard> getFlashCards(SharedPreferences sharedPreferences) {
        Gson gson = new Gson();
        List<String> allRawCards = new ArrayList<String>((Collection<String>) sharedPreferences.getAll().values());
        List<FlashCard> listOfCards = new ArrayList<>();
        for (int i = 0; i < allRawCards.size(); i++) {
            FlashCard card = gson.fromJson(allRawCards.get(i), FlashCard.class);
            listOfCards.add(card);
        }
        return listOfCards;
    }

    public static String getCardFileName() {
        return CARD_FILE_NAME;
    }

    public static void deleteCard(String uuid, SharedPreferences sharedPreferences)
    {
        sharedPreferences.edit().remove(uuid).apply();
    }
}
