package com.lovejiaming.timemovieinkotlin.views.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.lovejiaming.timemovieinkotlin.R
import com.zhy.autolayout.AutoLayoutActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_play_video.*
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class PlayVideoActivity : AutoLayoutActivity() {

    lateinit var disposble: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        setTimerProgress()
    }

    @SuppressLint("SimpleDateFormat")
    fun setTimerProgress() {
        disposble = Observable.interval(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    playprogress.max = videoview.duration
                    playprogress.progress = videoview.currentPosition
                    currenttime.text = SimpleDateFormat("mm:ss").format(videoview.currentPosition)
                    alltime.text = SimpleDateFormat("mm:ss").format(videoview.duration)
                }
        playprogress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2)
                    videoview.seekTo(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        videoview.setOnCompletionListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        videoview.setVideoURI(Uri.parse(intent.getStringExtra("path")))
        videoview.start()
        videoview.requestFocus()
        //
        videotitle.text = "<< ${intent.getStringExtra("name")} >>"
        videoview.setOnPreparedListener {
            preparetext.visibility = View.GONE
            prepareprogress.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoview.stopPlayback()
        disposble.dispose()
    }
}
