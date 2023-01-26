package com.example.myloyaltycards_last.model;

import android.content.Context;
import android.util.Log;

import com.example.myloyaltycards_last.MainActivity;

import java.util.ArrayList;

public class CardsListManager {
    private ArrayList<com.example.myloyaltycards_last.model.Card> list = null;
    private static CardsListManager instance = null;

    private CardsListManager() {
        list = new ArrayList<com.example.myloyaltycards_last.model.Card>();

        Log.d(MainActivity.TAG,"Cards list Created !");

    }


    public static CardsListManager getInstance(){
        if(instance == null)
            instance = new CardsListManager();
        return instance;
    }


    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public void add(com.example.myloyaltycards_last.model.Card card) {
        // Aggiunge elemento in coda
        this.list.add(card);
    }

    public Card remove(int index) {
        Card cardToDelete = getCard(index);
        this.list.remove(index);
        this.list.trimToSize();
        return cardToDelete;
    }

    public int size() {
        return this.list.size();
    }

    public int indexOf(com.example.myloyaltycards_last.model.Card card) {
        return this.list.indexOf(card);
    }

    public com.example.myloyaltycards_last.model.Card getCard(int index) {
        return this.list.get(index);
    }

    public ArrayList<Card> getList() {
        return this.list;
    }
}
