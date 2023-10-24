package com.freshuit.game

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.navigation.Navigation
import com.freshuit.game.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    var ind = 0
    var balance = 100_000

    private lateinit var player: MediaPlayer

    override fun onStop() {
        player.stop()
        super.onStop()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater,container,false)
        player = MediaPlayer.create(requireContext(),R.raw.bg)
        player.setOnCompletionListener { it.start() }
        var music = requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("music",0)
        if(music>0) player.start()
         balance = requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE)
            .getInt("balance",100_000)
        binding.balance5.text = balance.toString()
        val activity = requireActivity()
        binding.game.setEndListener(object : GameView.Companion.EndListener {
            override fun end() {
                activity.runOnUiThread {
                    binding.balance9.text = binding.game.score .toString()
                    binding.balance10.visibility = View.VISIBLE
                    binding.balance9.visibility = View.VISIBLE
                    binding.lay.visibility = View.VISIBLE
                }
            }

            override fun score() {
                activity.runOnUiThread {
                    binding.balance7.text = "00:${30-binding.game.millis/50}"
                    binding.balance8.text = binding.game.score .toString()
                }
            }

        })
        val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
        binding.imageButton2.setOnClickListener { navController.popBackStack() }
        binding.imageButton3.setOnClickListener { navController.popBackStack() }
          binding.slots2.setOnClickListener {
              binding.game.millis = 0
              binding.game.score = 0
            binding.game.ind = ind
            binding.lay.visibility = View.INVISIBLE
            binding.game.visibility = View.VISIBLE
            binding.game.togglePause()
        }
        return binding.root
    }

}