package com.lovejiaming.timemovieinkotlin.views.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
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
    val mGestureDetector: GestureDetector by lazy {
        GestureDetector(this, gestureListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        setTimerProgress()
        setGestureDetector()
    }

    fun setGestureDetector() {
        videoview.setOnTouchListener { _, motionEvent ->
            mGestureDetector.onTouchEvent(motionEvent)
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    m_bIsHandScroll = false
                    videoview.start()
                    return@setOnTouchListener true
                }
                else -> {
                    return@setOnTouchListener true
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun setTimerProgress() {
        disposble = Observable.interval(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (m_bIsHandScroll == false) {
                        playprogress.max = videoview.duration
                        playprogress.progress = videoview.currentPosition
                        currenttime.text = SimpleDateFormat("mm:ss").format(videoview.currentPosition)
                        alltime.text = SimpleDateFormat("mm:ss").format(videoview.duration)
                    }
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

    var m_bIsHandScroll = false
    //
    val gestureListener = object : GestureDetector.OnGestureListener {
        override fun onShowPress(p0: MotionEvent?) {
        }

        override fun onSingleTapUp(p0: MotionEvent?): Boolean {
            return true
        }

        override fun onDown(p0: MotionEvent?): Boolean {
            m_bIsHandScroll = true
            videoview.pause()
            return true
        }

        override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return true
        }

        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            Log.i("distancex == ", p2.toString())
            m_bIsHandScroll = true
            if (p2 < 0) {
                videoview.seekTo(videoview.currentPosition + 50)
            } else {
                videoview.seekTo(videoview.currentPosition - 150)
            }
            return true
        }

        override fun onLongPress(p0: MotionEvent?) {
        }

    }
}
