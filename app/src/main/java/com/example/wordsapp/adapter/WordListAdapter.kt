package com.example.wordsapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.R
import com.example.wordsapp.databinding.WordItemBinding
import com.example.wordsapp.model.Word

class WordListAdapter(var list: ArrayList<Word>, val onClick: (word: Word, postion: Int) -> Unit): RecyclerView.Adapter<WordListAdapter.ViewHolder>() {


    inner class ViewHolder(private var binding: WordItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun onBind(word: Word, position: Int)
        {
            binding.word.text = word.word
            binding.translate.text = word.translate

            itemView.setOnClickListener{
                onClick(word, position)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(WordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}