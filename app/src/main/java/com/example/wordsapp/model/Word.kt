package com.example.wordsapp.model

class Word {
    var id: Int = 0
    var word: String? = null
    var translate: String? = null
    var other: String? = null

    constructor(word: String?, translate: String?, other: String?) {

        this.word = word
        this.translate = translate
        this.other = other
    }

    constructor(id: Int, word: String?, translate: String?, other: String?) {
        this.id = id
        this.word = word
        this.translate = translate
        this.other = other
    }

    constructor()
}