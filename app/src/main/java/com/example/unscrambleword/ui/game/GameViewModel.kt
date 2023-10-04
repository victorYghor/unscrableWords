package com.example.unscrambleword.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    /*store the score of the game
    * */
    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    /*
    * this store the word in a shuffled way
    * */
    private val _scrambleWord = MutableLiveData<String>()
    val scrambleWord: LiveData<String> = _scrambleWord

    /*this store the list of words that the game get
    * */
    private val _wordsList = MutableLiveData(mutableListOf<String>())
    val wordsList: LiveData<MutableList<String>> = _wordsList

    /*this store the current word count
    * */
    private val _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount: LiveData<Int> = _currentWordCount

    /*
    * this store the current word in the game that is not shuffled
    * */
    private val _currentWord = MutableLiveData<String>("")
    val currentWord: LiveData<String> = _currentWord



    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    private fun getNextWord() {
        _currentWord.value = allWordsList.random()
        val tempWord = currentWord.value!!.toCharArray()
        tempWord.shuffle()

        /// todo here I make something different
        //here I am shuffle the word and see if this is different of the current word
        while (String(tempWord) == currentWord.value!!) {
            tempWord.shuffle()
        }
        if(_wordsList.value!!.contains(currentWord.value)) {
            getNextWord()
        } else {
            // putting the word in the words list for not repeat words
            _wordsList.value!!.add(_currentWord.value!!)
            _currentWordCount.value = _currentWordCount.value?.inc()
            _scrambleWord.value = String(tempWord)
        }
    }

    fun hasNextWord(): Boolean{
        return if(_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else {
            false
        }
    }

    private fun increaseScore(playerWord: String): Boolean {
        if(playerWord == _currentWord.value) {
            _score.value = SCORE_INCREASE + _score.value!!
            return true
        } else {
            return false
        }
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if(playerWord == currentWord.value) {
            increaseScore(playerWord)
            return true
        } else {
            return false
        }
    }

    fun reinitializeData() {
        _score.value = 0
        _scrambleWord.value = ""
        _currentWord.value = ""
        _wordsList.value = mutableListOf<String>()
        _currentWordCount.value = 0
        getNextWord()
    }


}