package com.example.wordsapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.wordsapp.R
import com.example.wordsapp.adapter.WordListAdapter
import com.example.wordsapp.database.DatabaseHelper
import com.example.wordsapp.databinding.BottomSheetDialogBinding
import com.example.wordsapp.databinding.FragmentListBinding
import com.example.wordsapp.model.Word
import com.google.android.material.bottomsheet.BottomSheetDialog

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: WordListAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var newArrayList: ArrayList<Word>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(layoutInflater, container, false)

        databaseHelper = DatabaseHelper(requireContext())


        setAdapter(databaseHelper.allWords())
        binding.add.setOnClickListener {
            findNavController().navigate(R.id.addFragment)
        }

        return binding.root
    }

    private fun setAdapter(list: List<Word>) {
        adapter = WordListAdapter(list as ArrayList<Word>){ it, position ->
            showBottomSheetDialog(it)
        }
        binding.rv.adapter = adapter

    }

    private fun showBottomSheetDialog(words: Word) {
        val dbHelper = DatabaseHelper(requireContext())
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = BottomSheetDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        val ochir = dialogView.delete
        val jonat = dialogView.share
        val tahrir = dialogView.edit

        dialogView.maqoleng.text = words.word
        dialogView.maqoluz.text = words.translate
        dialogView.maqolru.text = words.other
        ochir.setOnClickListener {

            dbHelper.delete(words)
            setAdapter(dbHelper.allWords())
            Toast.makeText(requireContext(), "Word deleted", Toast.LENGTH_SHORT).show()
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
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.edit_word_dialog, null, false)

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
                databaseHelper.edit(words, words.id)
                Toast.makeText(requireContext(), "Word changed", Toast.LENGTH_SHORT).show()
                setAdapter(databaseHelper.allWords())
                dialog.cancel()
            } else {
                Toast.makeText(requireContext(), "Iltimos hamma maydonlarni to'ldiring", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.setView(dialogView)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

}