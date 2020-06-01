package com.rohit.quizchampion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.rohit.quizchampion.databinding.FragmentGameOverBinding

class GameOverFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentGameOverBinding>(inflater,
            R.layout.fragment_game_over,container,false)

        binding.nextMatchButton.setOnClickListener{view:View ->
            view.findNavController().navigate(GameOverFragmentDirections.actionGameOverFragmentToQuizFragment())
        }

        val args = GameOverFragmentArgs.fromBundle(arguments!!)
        binding.scoreField.text = "Correct Answers - ${args.correctAnswers.toString()}\nWrong Answers - ${args.wrongAnswers.toString()}\nTotal Score "+((args.correctAnswers.toInt() * 2) - args.wrongAnswers).toString()

        return binding.root
    }
}