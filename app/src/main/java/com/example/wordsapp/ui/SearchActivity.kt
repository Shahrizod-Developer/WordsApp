package com.example.wordsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.wordsapp.R
import com.example.wordsapp.adapter.WordListAdapter
import com.example.wordsapp.database.DatabaseHelper
import com.example.wordsapp.databinding.ActivitySearchBinding
import com.example.wordsapp.databinding.BottomSheetDialogBinding
import com.example.wordsapp.databinding.FragmentSearchBinding
import com.example.wordsapp.model.Word
import com.google.android.material.bottomsheet.BottomSheetDialog

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listWOrd : ArrayList<Word>
    private lateinit var adapter: WordListAdapter
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        val list = dbHelper.allWords()

        binding.imageSearch.setOnClickListener {

            val word = binding.search.text.toString()

            for (i in 0 until list.size)
            {
                if(word == list[i].word)
                {
                    listWOrd[count] = list[i]
                    count++
                }
            }

            setAdapter(list)
        }


    }
    private fun setAdapter(list: List<Word>) {
        adapter = WordListAdapter(list as ArrayList<Word>){ it, position ->
            showBottomSheetDialog(it)
        }
        binding.rv.adapter = adapter

    }

    private fun showBottomSheetDialog(words: Word) {
        val dbHelper = DatabaseHelper(this)
        val dialog = BottomSheetDialog(this)
        val dialogView = BottomSheetDialogBinding.inflate(LayoutInflater.from(this), null, false)
        val ochir = dialogView.delete
        val jonat = dialogView.share
        val tahrir = dialogView.edit

        dialogView.maqoleng.text = words.word
        dialogView.maqoluz.text = words.translate
        dialogView.maqolru.text = words.other
        ochir.setOnClickListener {

            dbHelper.delete(words)
            setAdapter(dbHelper.allWords())
            Toast.makeText(this, "Word deleted", Toast.LENGTH_SHORT).show()
            dialog.cancel()
        }
        tahrir.setOnClickListener {

            dialog.cancel()
            showDialog(words)

        }
        jonat.setOnClickListener {
            val intent= Intent()
            intent.action= Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, words.word + "\n" + words.translate)

            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent,"Share To:"))
        }
        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    private fun showDialog(words: Word) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogView = LayoutInflater.from(this).inflate(R.layout.edit_word_dialog, null, false)

        val saqla = dialogView.findViewById<Button>(R.id.save)
        val yop = dialogView.findViewById<Button>(R.id.cancel)
        val eng = dialogView.findViewById<EditText>(R.id.wordeng)
        val uz = dialogView.findViewById<EditText>(R.id.wordeuz)
        val ru = dialogView.findViewById<EditText>(R.id.wordru)

        eng.setText(words.word)
        uz.setText(words.translate)
        ru.setText(words.other)

        yop.setOnClickListener {
            dialog.cancel()
        }
        saqla.setOnClickListener {

            if (eng.text.toString().isNotEmpty() && uz.text.toString().isNotEmpty()) {
                words.word = eng.text.toString()
                words.translate = uz.text.toString()
                words.other = ru.text.toString()
                dbHelper.edit(words, words.id)
                Toast.makeText(this, "Word changed", Toast.LENGTH_SHORT).show()
                setAdapter(dbHelper.allWords())
                dialog.cancel()
            } else {
                Toast.makeText(this, "Iltimos hamma maydonlarni to'ldiring", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.setView(dialogView)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

}