package com.autodhil.tutarisiswa.ui.fragment.latihan

import android.content.ContentValues.TAG
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.autodhil.tutarisiswa.R
import com.autodhil.tutarisiswa.databinding.FragmentLatihanBinding
import com.autodhil.tutarisiswa.model.Song
import com.autodhil.tutarisiswa.utils.other.FlashUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pajaga.utils.other.toSongTime


class LatihanFragment : Fragment() {

    private var songs : Song? = null
    private var musicPlayer : MediaPlayer? = null
    private lateinit var handler: Handler
    private lateinit var databaseMySong : DatabaseReference
    private lateinit var binding : FragmentLatihanBinding

    private val eventListenerMusic = object  : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val gson = Gson().toJson(snapshot.value)
            val type = object : TypeToken<MutableList<Song>>(){}.type
            val song = Gson().fromJson<MutableList<Song>>(gson,type)
            if (!song.isNullOrEmpty()){
                songs = song[0]
                songs?.let { playMusic(it) }
                Log.d(TAG, "onDataChange: apa lagunya ${songs}")
            }

        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLatihanBinding.inflate(inflater,container,false)
        handler = Handler(Looper.getMainLooper())
        musicPlayer = MediaPlayer()
        musicPlayer?.setAudioAttributes(
            AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        getData()


        return binding.root
    }

    override fun onPause() {
        super.onPause()
        musicPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer?.stop()
        musicPlayer = null
    }

    override fun onResume() {
        super.onResume()
        if (musicPlayer != null && !musicPlayer?.isPlaying!!){
            musicPlayer?.start()
        }
    }


    private fun playMusic(song: Song) {
        binding.tvJudul.text = songs?.nameSong
        try {
            musicPlayer?.setDataSource(song.uriSong)
            musicPlayer?.setOnPreparedListener{
                it.start()
                binding.tvStartTime.text = it?.duration?.toSongTime()
                binding.seekBarPlaySong.max = it?.duration!!
                checkMusicbutton()
            }

            musicPlayer?.prepareAsync()
            handler.postDelayed(object  : Runnable{
                override fun run() {
                    try {
                        binding.seekBarPlaySong.progress = musicPlayer?.currentPosition!!
                        handler.postDelayed(this,1000)
                    }catch (e : Exception){
                        Log.e(TAG, "run: ${e.message}", )
                    }

                }

            },0)

            binding.seekBarPlaySong.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if(musicPlayer != null){
                        val currentTime = progress.toSongTime()
                        val maxdDuration = musicPlayer?.duration
                        Log.d(TAG, "onProgressChanged: ${progress.toSongTime()}")
                        when(progress.toSongTime()){
                            "0:03" -> context?.let { FlashUtils.openFlashLight(it) }
                            "0:04" -> context?.let { FlashUtils.closeFlashLight(it) }
                            "0:10" -> context?.let { FlashUtils.openFlashLight(it) }
                            "0:15" -> context?.let { FlashUtils.closeFlashLight(it) }
                        }

                        binding.tvStartTime.text = currentTime

                        if (!musicPlayer?.isPlaying!!){
                            binding.tvStartTime.text = musicPlayer?.currentPosition?.toSongTime()
                        }

                        if (fromUser){
                            musicPlayer?.seekTo(progress)
                            seekbar?.progress
                        }

                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    TODO("Not yet implemented")
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    TODO("Not yet implemented")
                }

            })
        }catch (e : java.lang.IllegalStateException){
            Log.e(TAG, "playMusic: ${e.message}", )
        }

    }

    private fun checkMusicbutton() {
        if (musicPlayer?.isPlaying!! && musicPlayer != null){
            binding.ivPlaySong.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.ic_baseline_pause_circle_filled_24) })
        }else{
            binding.ivPlaySong.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.ic_baseline_play_circle_filled_24) })
        }
    }

    private fun getData() {
        databaseMySong = FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("tes")
        databaseMySong.addValueEventListener(eventListenerMusic)


    }


}


