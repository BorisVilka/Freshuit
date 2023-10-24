package com.freshuit.game

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.freshuit.game.databinding.FragmentSlotsBinding

class SlotsFragment : Fragment() {

    var balance = 100_000

    private lateinit var binding: FragmentSlotsBinding

    var l = 558

    private lateinit var player: MediaPlayer
    private lateinit var slot: MediaPlayer
    private lateinit var win: MediaPlayer

    override fun onStop() {
        player.stop()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSlotsBinding.inflate(inflater,container,false)
        player = MediaPlayer.create(requireContext(),R.raw.bg)
        slot = MediaPlayer.create(requireContext(),R.raw.slot)
        win = MediaPlayer.create(requireContext(),R.raw.win)
        player.setOnCompletionListener { it.start() }
        var music = requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("music",0)
        if(music>0) player.start()
        balance = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getInt("balance",100_000)
        binding.balance.text = balance.toString()
        binding.imageButton.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        binding.list1.adapter = MyAdapter()
        binding.list2.adapter = MyAdapter()
        binding.list3.adapter = MyAdapter()
        binding.list4.adapter = MyAdapter()
        binding.list5.adapter = MyAdapter()
        binding.list1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState==RecyclerView.SCREEN_STATE_OFF) {
                    Log.d("TAG","OFF")
                    if(
                        (binding.list1.adapter as MyAdapter).data[l]==
                        (binding.list2.adapter as MyAdapter).data[l]
                        &&
                        (binding.list1.adapter as MyAdapter).data[l]==
                        (binding.list3.adapter as MyAdapter).data[l]
                        &&
                        (binding.list1.adapter as MyAdapter).data[l]==
                        (binding.list4.adapter as MyAdapter).data[l]
                        &&
                        (binding.list1.adapter as MyAdapter).data[l]==
                        (binding.list5.adapter as MyAdapter).data[l]
                    ) {
                        if(music>1) {
                            win.seekTo(0)
                            win.start()
                        }
                        balance += 200
                        requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE)
                            .edit().putInt("balance",balance).apply()
                        binding.balance.text = balance.toString()
                    }
                }
            }
        })
        binding.imageView10.setOnClickListener {
            if(music>1) {
                slot.seekTo(0)
                slot.start()
            }
            binding.list1.adapter = MyAdapter()
            binding.list2.adapter = MyAdapter()
            binding.list3.adapter = MyAdapter()
            binding.list4.adapter = MyAdapter()
            binding.list5.adapter = MyAdapter()
            binding.list1.scrollToPosition(0)
            binding.list2.scrollToPosition(0)
            binding.list3.scrollToPosition(0)
            binding.list4.scrollToPosition(0)
            binding.list5.scrollToPosition(0)
            balance -= 100
            requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE)
                .edit().putInt("balance",balance).apply()
            binding.balance.text = balance.toString()
            binding.list1.smoothScrollToPosition(l+1)
            binding.list2.smoothScrollToPosition(l+1)
            binding.list3.smoothScrollToPosition(l+1)
            binding.list4.smoothScrollToPosition(l+1)
            binding.list5.smoothScrollToPosition(l+1)
        }
        return binding.root
    }

}