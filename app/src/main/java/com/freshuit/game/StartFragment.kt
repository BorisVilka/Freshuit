package com.freshuit.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.freshuit.game.databinding.FragmentStartBinding


class StartFragment : Fragment() {


    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater,container,false)
        val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
        binding.slots.setOnClickListener {
            navController.navigate(R.id.slotsFragment)
        }
        binding.game.setOnClickListener {
            navController.navigate(R.id.gameFragment)
        }
        var music = requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("music",0)
        setM(music)
        binding.t1.setOnClickListener {
            music = 0
            requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("music",music).apply()
            setM(music)
        }
        binding.t2.setOnClickListener {
            music = 1
            requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("music",music).apply()
            setM(music)
        }
        binding.t3.setOnClickListener {
            music = 2
            requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("music",music).apply()
            setM(music)
        }
        return binding.root
    }

    fun setM(music: Int) {
        binding.t1.setBackgroundResource(R.drawable.offbg)
        binding.t2.setBackgroundResource(R.drawable.offbg)
        binding.t3.setBackgroundResource(R.drawable.offbg)
        when(music) {
            0 -> binding.t1.setBackgroundResource(R.drawable.on)
            1 -> binding.t2.setBackgroundResource(R.drawable.on)
            2 -> binding.t3.setBackgroundResource(R.drawable.on)
        }
    }

}