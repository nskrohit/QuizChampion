package com.rohit.quizchampion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.rohit.quizchampion.databinding.FragmentRulesBinding

class RulesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentRulesBinding>(inflater,
            R.layout.fragment_rules,container,false)

        binding.playButton2.setOnClickListener{view:View ->
            view.findNavController().navigate(RulesFragmentDirections.actionRulesFragmentToQuizFragment())
        }

        return binding.root
    }
}