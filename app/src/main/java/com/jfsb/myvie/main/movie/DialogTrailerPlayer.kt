package com.jfsb.myvie.main.movie

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.jfsb.myvie.R
import com.jfsb.myvie.databinding.LayoutTrailerPlayerBinding
import java.util.*
import androidx.annotation.NonNull
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener




class DialogTrailerPlayer(linkTrailer:String) : DialogFragment(){

    val link = linkTrailer
    private var _binding : LayoutTrailerPlayerBinding? = null
    val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertBuilder = AlertDialog.Builder(it)

            _binding = LayoutTrailerPlayerBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)

            lifecycle.addObserver(binding.plTrailer)
            binding.plTrailer.getPlayerUiController()

            binding.plTrailer.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = link
                    youTubePlayer.loadVideo(videoId, 0.0F)
                }
            })
            //alertBuilder.setView(requireActivity().layoutInflater.inflate(R.layout.layout_create,null))

            /*alertBuilder.setTitle("TRAILER")
            alertBuilder.setIcon(R.mipmap.logo)*/

        /*    alertBuilder.setPositiveButton("Cerrar", DialogInterface.OnClickListener { dialog, id ->

            })*/

            alertBuilder.create()



        } ?: throw IllegalStateException("Exception !! Activity is null")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}