package com.autodhil.tutarisiswa.ui.activity.minggu2

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Outline
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.AspectRatioFrameLayout
import com.autodhil.tutarisiswa.R
import com.autodhil.tutarisiswa.databinding.ActivityMinggu2Binding
import com.autodhil.tutarisiswa.model.Tugas
import com.autodhil.tutarisiswa.utils.local.SessionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pajaga.utils.other.Constant


@UnstableApi class Minggu2Activity : AppCompatActivity() {
    private lateinit var session : SessionManager
    private lateinit var binding : ActivityMinggu2Binding
    private   var data : Tugas? = null
    private val playbackStateListener: Player.Listener = playbackStateListener()
    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    private lateinit var uri: Uri
    private var uriString : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinggu2Binding.inflate(layoutInflater)
        session = SessionManager(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data = intent.getParcelableExtra<Tugas>(KEY_TUGAS,Tugas::class.java)
        }else{
            data = intent.getParcelableExtra<Tugas>(KEY_TUGAS)
        }
        binding.videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        initView()

        binding.button.setOnClickListener {
            if (session.username.equals("admin")){
                val reference =
                    FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .reference

                data?.username?.let { it1 ->
                    reference
                        .child(Constant.dbnode_tugas)
                        .child(it1)
                        .child(data?.idTugas.toString())
                        .child("comment")
                        .setValue(binding.komentar?.text.toString())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                            binding.button.isEnabled = false
                            binding.komentar?.isEnabled = false
                        }.addOnFailureListener {
                            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                }
            }else{
                startActivity(Intent(this,RecordActivity::class.java)
                    .putExtra(KEY_TUGAS,data)
                )
            }

        }
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.videoView.setOutlineProvider(object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 25f)
                }
            })
        } else {
            // In portrait
        }



        binding.videoView.setClipToOutline(true)






        setContentView(binding.root)

    }

    private fun initView() {
        val reference = FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
            Constant.dbnode_tugas)
        val referenceTugas = data?.username?.let { reference.child(it).child(data?.idTugas.toString())  }

        referenceTugas?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val tugas = dataSnapshot.getValue(Tugas::class.java)
                if (!tugas?.uriVideo.isNullOrEmpty()){
                    //set Video
                    uriString = tugas?.uriVideo.toString()
                    initializePlayer(uriString)
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

                    //untuk guru
                    if (session.username.equals("admin")){
                        binding.button.isEnabled = true
                        binding.komentar?.isEnabled = true
                        if (!tugas?.comment.isNullOrEmpty()){
                            binding.komentar?.setText(tugas?.comment)
                            binding.komentar?.isEnabled = false
                            binding.button.isEnabled = false
                        }
                    }else{

                        binding.button.visibility = View.GONE
                        if (!tugas?.comment.isNullOrEmpty()){
                            binding.komentar?.setText(tugas?.comment)
                            binding.komentar?.isEnabled = false
                            binding.button.visibility = View.GONE

                        }else{
                            binding.komentar?.setText("Belum diberi komentar")
                        }
                    }

                }else{
                    if (session.username.equals("admin")){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        binding.tvBelumAda?.visibility = View.VISIBLE
                        binding.frame?.visibility = View.INVISIBLE
                    }else{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                    }



                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "onCancelled adakah", databaseError.toException())
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer(uriString)

        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer(uriString)

        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()

        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()

        }
    }

    private fun initializePlayer(stringUri: String = "") {
        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        player = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->
                binding.videoView.player = exoPlayer

                val videoPath =
                    "android.resource://" + this?.packageName.toString() + "/" + R.raw.briskwalking

                val path = RawResourceDataSource.buildRawResourceUri(R.raw.briskwalking).toString()
                val uriRaw = RawResourceDataSource.buildRawResourceUri(R.raw.briskwalking)

                if (stringUri.isNullOrEmpty()){
                    uri = Uri.parse(videoPath)
                }else{
                    uri = Uri.parse(stringUri)
                }


//                val mediaItem = MediaItem.Builder()
//                    .setUri(uriRaw)
//                    .setMimeType(MimeTypes.APPLICATION_MPD)
//                    .build()

                val mediaItem = MediaItem.fromUri(uri)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.addListener(playbackStateListener)
                exoPlayer.prepare()



            }
        player?.pause()

    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.removeListener(playbackStateListener)
            exoPlayer.release()
        }
        player = null
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }



    companion object {
        const val KEY_TUGAS = "news_key"
        const val KEY_URI = "URI"
    }
}


private fun playbackStateListener() = object : Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
            else -> "UNKNOWN_STATE             -"
        }
        Log.d(ContentValues.TAG, "changed state to $stateString")
    }
}