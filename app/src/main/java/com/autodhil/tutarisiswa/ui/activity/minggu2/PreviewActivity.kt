package com.autodhil.tutarisiswa.ui.activity.minggu2

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Outline
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import android.webkit.MimeTypeMap
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
import com.autodhil.tutarisiswa.R
import com.autodhil.tutarisiswa.databinding.ActivityPreviewBinding
import com.autodhil.tutarisiswa.model.Tugas
import com.autodhil.tutarisiswa.utils.local.SessionManager
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.UploadTask
import com.pajaga.utils.other.Constant


@UnstableApi class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private val playbackStateListener: Player.Listener = playbackStateListener()
    private var player: ExoPlayer? = null
    private lateinit var session : SessionManager

    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    private  var data : Tugas? = null
    private  var uri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)

        session = SessionManager(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data = intent.getParcelableExtra<Tugas>(Minggu2Activity.KEY_TUGAS,Tugas::class.java)
            uri = intent.getParcelableExtra(Minggu2Activity.KEY_URI,Uri::class.java)
        }else{
            data = intent.getParcelableExtra<Tugas>(Minggu2Activity.KEY_TUGAS)
            uri = intent.getParcelableExtra(Minggu2Activity.KEY_URI)
        }
        binding.button.setOnClickListener {
            uploadvideo()
            Toast.makeText(this, "finish", Toast.LENGTH_SHORT).show()

        }

        binding.videoView.setOutlineProvider(object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 25f)
            }
        })

        binding.videoView.setClipToOutline(true)

//        binding.button.setOnClickListener {
//            sendVideo()
//        }






        setContentView(binding.root)

    }

    private fun getfiletype(videouri: Uri): String? {
        val r = contentResolver
        // get the file type ,in this case its mp4
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(r.getType(videouri))
    }

    fun uploadvideo() {
        binding.progressDialog.visibility = View.VISIBLE
        if (uri != null) {
            // save the selected video in Firebase storage
            val reference = data?.title?.let {
                session.username?.let { it1 ->
                    FirebaseStorage.getInstance("gs://tutari-cfcf6.appspot.com")
                        .reference
                        .child("video")
                        .child(it1)
                        .child(it)
                }
            }
            reference?.putFile(uri!!)?.addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful());
                // get the link of video
                val downloadUri: String = uriTask.getResult().toString()
                val reference1 = FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Video")

                uploadtoDatabase(downloadUri)
//                val map: HashMap<String, String> = HashMap()
//                map["videolink"] = downloadUri
//                reference1.child("" + System.currentTimeMillis()).setValue(map)
                // Video uploaded successfully
                // Dismiss dialog
                binding.progressDialog.visibility = View.GONE
                Toast.makeText(this , "Video Uploaded!!", Toast.LENGTH_SHORT).show()
            }?.addOnFailureListener { e -> // Error, Image not uploaded
                binding.progressDialog.visibility = View.GONE
                Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT).show()
            }?.addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
                // Progress Listener for loading
                // percentage on the dialog box
                override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
                    // show the progress bar
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    binding.persen.visibility = View.VISIBLE
                    binding.persen.text = "Upload  ${progress.toInt()} %"
//                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
            })
        }
    }




    private fun uploadtoDatabase(downloadUri: String) {
        val reference =
            FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference()
        //input Tugas
        val tugas = data?.username?.let {
            Tugas(
                1,
                "",
                it,
                downloadUri,
                ""
            )
        }
        data?.username?.let {
            reference
                .child(Constant.dbnode_tugas)
                .child(it)
                .child(data!!.idTugas.toString())
                .setValue(tugas) { databaseError, databaseReference ->
                    if (databaseError != null) {
                        Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Data berhasil", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        finish()

    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()

        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
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
            Toast.makeText(this, "Stop  if", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializePlayer() {
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
//                val uri: Uri = Uri.parse(videoPath)

//                val mediaItem = MediaItem.Builder()
//                    .setUri(uriRaw)
//                    .setMimeType(MimeTypes.APPLICATION_MPD)
//                    .build()

                val mediaItem = uri?.let { MediaItem.fromUri(it) }
                mediaItem?.let { exoPlayer.setMediaItem(it) }
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
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
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