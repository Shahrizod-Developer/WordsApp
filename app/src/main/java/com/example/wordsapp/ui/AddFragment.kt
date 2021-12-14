package com.example.wordsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.wordsapp.R
import com.example.wordsapp.database.DatabaseHelper
import com.example.wordsapp.databinding.FragmentAddBinding
import com.example.wordsapp.model.Word

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(layoutInflater, container, false)

        databaseHelper = DatabaseHelper(requireContext())


        binding.save.setOnClickListener {

            val word = binding.word.text.toString()
            val translate = binding.translate.text.toString()
            val other = binding.other.text.toString()

            if(word.isNotEmpty() && translate.isNotEmpty())
            {
                val wordObj = Word(word, translate, other)
                databaseHelper.insert(wordObj)
                binding.word.text = null
                binding.translate.text = null
                binding.other.text = null
                Toast.makeText(requireContext(), "$word Added", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), "Please fill all fields !(word and translate)", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


}