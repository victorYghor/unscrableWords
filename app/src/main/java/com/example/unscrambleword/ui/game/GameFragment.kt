package com.example.unscrambleword.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.unscrambleword.R
import com.example.unscrambleword.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class GameFragment : Fragment() {
    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("GameFragment", "Word: ${viewModel.scrambleWord} ")
        // inflate the layout file and return a binding instance
        binding = FragmentGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // make the data accessible to the layout
        binding.gameViewModel = viewModel
        binding.maxNumberOfWords = MAX_NO_OF_WORDS
        // Specify the fragment view as owner of the binding
        // this makes make the GameFragment observe the data in the view
        binding.lifecycleOwner = viewLifecycleOwner
        binding.btnSkip.setOnClickListener { onSkipWord() }
        binding.btnSubmit.setOnClickListener { onSubmitWord() }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        Log.d(
            "GameFragment",
            "Here I am in onStart Now the fragment will be create to be used ${viewModel.scrambleWord}"
        )
        viewModel.scrambleWord.observe(this) { value ->
            Log.d("GameFragment", "scrambleWord: $value")
        }
        super.onStart()
    }

    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()
        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (!viewModel.hasNextWord()) {
                this.showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    private fun showFinalScoreDialog() {
        Log.d("codeReach", "showfinalscoredialog")
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.congratulations))
            setMessage(getString(R.string.you_scored, viewModel.score.value?.toInt()))
            setNegativeButton(R.string.exit) { _, _ ->
                exitGame()
            }
            setPositiveButton(R.string.play_again) { _, _ ->
                restart()
            }
        }.show()

        // for show the dialogs you need call show method
    }

    private fun restart() {
        setErrorTextField(false)
        viewModel.reinitializeData()
    }


    /*put the error message if the word is wrong
    * */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.error = getString(R.string.try_again)
            binding.textField.isErrorEnabled = true
            binding.textInputEditText.text = null
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    private fun onSkipWord() {
        if (viewModel.hasNextWord()) {
            setErrorTextField(false)
        } else {
           showFinalScoreDialog()
        }
    }

    private fun exitGame() {
        activity?.finish()
    }


}