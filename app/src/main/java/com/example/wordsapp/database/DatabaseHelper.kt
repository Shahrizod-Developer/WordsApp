package com.example.wordsapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.wordsapp.model.Word
import kotlinx.coroutines.channels.consumesAll

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION), DatabaseService {
    override fun onCreate(db: SQLiteDatabase?) {

        val query = "create table ${Constants.TABLE_NAME}(${Constants.ID} integer primary key autoincrement ," +
                "${Constants.WORD} text, ${Constants.TRANSLATE} text, ${Constants.OTHER} text ) "
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    override fun insert(word: Word) {

        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(Constants.WORD, word.word)
        contentValue.put(Constants.TRANSLATE, word.translate)
        contentValue.put(Constants.OTHER, word.other)

        db.insert(Constants.TABLE_NAME, null, contentValue)
        db.close()
    }

    @SuppressLint("Recycle")
    override fun allWords(): ArrayList<Word> {

        val list = ArrayList<Word>()
        val db =this.writableDatabase
        val query = "select*from ${Constants.TABLE_NAME}"
        val raw =db.rawQuery(query, null)

        if(raw.moveToFirst())
        {
            do {

                val id = raw.getInt(0)
                val word = raw.getString(1)
                val translate = raw.getString(2)
                val other = raw.getString(3)

                val wordObj = Word(id, word, translate, other)
                list.add(wordObj)
            }
                while (raw.moveToNext())
        }
        return list
    }

    override fun delete(word: Word) {

        val db = this.readableDatabase
        db.delete(Constants.TABLE_NAME, "${Constants.WORD} LIKE ?", arrayOf(word.word))
        db.close()

    }

    override fun edit(word: Word, id: Int) {
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(Constants.WORD, word.word)
        contentValue.put(Constants.TRANSLATE, word.translate)
        contentValue.put(Constants.OTHER, word.other)

        db.update(Constants.TABLE_NAME, contentValue, "${Constants.ID} = $id", null)
        db.close()
    }

    fun search(keyword: String): MutableList<Word?>? {
        var devices: MutableList<Word?>? = null
        try {
            val sqLiteDatabase = readableDatabase
            val cursor = sqLiteDatabase.rawQuery(
                "select * from " + Constants.TABLE_NAME + " where " + Constants.WORD + " like ?",
                arrayOf("%$keyword%")
            )
            if (cursor.moveToFirst()) {
                devices = ArrayList<Word?>()
                do {
                    val word = Word()
                    word.word = cursor.getString(0)
                    word.translate = cursor.getString(1)
                    word.other = cursor.getString(2)

                    devices.add(word)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            devices = null
        }
        return devices
    }

    override fun readByWord(): List<Word> {
        val list = ArrayList<Word>()
        val query = "select *from " + Constants.WORD + " order by " + Constants.TRANSLATE
        val db = this.writableDatabase
        val raw = db.rawQuery(query, null)

        if (raw.moveToFirst()) {
            do {
                val id = raw.getInt(0)
                val word = raw.getString(1)
                val translate = raw.getString(2)
                val other = raw.getString(3)

                val wordObj = Word(id, word, translate, other)
                list.add(wordObj)
            } while (raw.moveToNext())
        }
        return list
    }

}