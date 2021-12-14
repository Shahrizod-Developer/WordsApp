package com.example.wordsapp.database

import com.example.wordsapp.model.Word

interface DatabaseService {

    fun insert(word: Word)
    fun allWords(): ArrayList<Word>
    fun delete(word: Word)
    fun edit(word: Word, id:Int)
    fun readByWord():List<Word>
   // fun readByTranslate(device: Word)
}