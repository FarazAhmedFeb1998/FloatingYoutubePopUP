package com.faraz.floatingyoutubepopup

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.floating_video_layout.view.*

class FloatingYoutubePopUp:AppCompatActivity() {

    var popupWindow: PopupWindow? = null
    private var popUpWidth = 0
    private var popUpHeight = 0
    var playAt = 0F
    var karmaVideoId = ""
    var clickCount = 0
    var startTime:Long = 0L
    var duration:Long = 0L
    var fastForward = false
    var fastBackWord = false


    fun floatingPopUp(context: Activity, mView: View, videoId: String): PopupWindow {
        var minOrMax = true

        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        popUpWidth = width / 2
        if (height / 2 > 1000) {
            popUpHeight = height / 2 - 150
        } else {
            popUpHeight = height / 2
        }


        val layoutInflater: LayoutInflater =
            context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popupView: View = layoutInflater.inflate(R.layout.floating_video_layout, null)

        popupWindow = PopupWindow(context.baseContext)
        popupWindow!!.contentView = popupView
        popupWindow!!.width = popUpWidth
        popupWindow!!.height = popUpHeight

        val youtubeVideoPlayer: YouTubePlayerView = popupView.findViewById(R.id.youtube_player1)

        karmaYouTubePlayerInitialization(youtubeVideoPlayer, videoId)

        val closePopUp = popupView.findViewById<ImageView>(R.id.closePopup)
        closePopUp.setOnClickListener {
            popupWindow!!.dismiss()
        }


        val maximizePopUp = popupView.findViewById<ImageView>(R.id.popUpMaximize)
        maximizePopUp.setOnClickListener {
            if (minOrMax) {
                popupWindow!!.dismiss()
                popupWindow = PopupWindow(
                    popupView,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT
                )
                popupWindow!!.showAtLocation(mView, Gravity.NO_GRAVITY, popUpWidth, popUpHeight)
                youtubeVideoPlayer.enterFullScreen()
                popupView.draggableFrameLeft.visibility = View.GONE
                popupView.draggableFrameRight.visibility = View.GONE
                minOrMax = false
                popupView.youtube_player1.layoutParams.height = ActionBar.LayoutParams.MATCH_PARENT
                popupView.youtube_player1.layoutParams.width = ActionBar.LayoutParams.MATCH_PARENT
                popupView.floatingCs.margin(30F, 30F, 30F, 30F)
            } else {
                popupWindow!!.dismiss()
                popupWindow = PopupWindow(popupView, popUpWidth, popUpHeight)
                popupView.draggableFrameLeft.visibility = View.VISIBLE
                popupView.draggableFrameRight.visibility = View.VISIBLE
                popupWindow!!.showAtLocation(mView, Gravity.NO_GRAVITY, popUpWidth, popUpHeight)
                minOrMax = true
                popupView.youtube_player1.layoutParams.height = popUpHeight
                popupView.youtube_player1.layoutParams.width = popUpWidth
                popupView.floatingCs.margin(0F, 0F, 0F, 0F)
            }


        }
        popupWindow!!.showAtLocation(mView, Gravity.NO_GRAVITY, popUpWidth, popUpHeight)


        popupView.youtube_player1.layoutParams.height = popUpHeight
        popupView.youtube_player1.layoutParams.width = popUpWidth
        val draggableWidth = width / 4 - 30
        val draggableHeight = popUpHeight - 120
        popupView.draggableFrameLeft.layoutParams.height = draggableHeight
        popupView.draggableFrameRight.layoutParams.height = draggableHeight
        popupView.draggableFrameLeft.layoutParams.width = draggableWidth
        popupView.draggableFrameRight.layoutParams.width = draggableWidth
        popupView.floatingCs.margin(0F, 0F, 0F, 0F)

        popupView.draggableFrameLeft.setOnTouchListener(object : View.OnTouchListener {
            var orgX = 0
            var orgY = 0
            var offsetX = 0
            var offsetY = 0
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        clickCount++
                        if (clickCount == 1){
                            startTime = System.currentTimeMillis()
                        }
                        else if (clickCount == 2){
                            duration = System.currentTimeMillis() - startTime
                            if (duration <= 700){
                                fastBackWord = true
                                popupView.backWordSecTv.text = "<<10s"
                                popupView.backWordSecTv.visibility = View.VISIBLE
                                clickCount = 0
                                duration = 0L
                            }
                            else{
                                clickCount = 1
                                startTime = System.currentTimeMillis()
                            }
                        }
                        if (popupView.backWordSecTv.isVisible){
                            delay(popupView.backWordSecTv)
                        }
                    }

                    MotionEvent.ACTION_DOWN -> {
                        orgX = event.x.toInt()
                        orgY = event.y.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        offsetX = (event.rawX - orgX).toInt()
                        offsetY = (event.rawY - orgY).toInt()
                        popupWindow!!.update(offsetX, offsetY, -1, -1, true)
                    }
                }
                return true
            }
        })

        popupView.draggableFrameRight.setOnTouchListener(object : View.OnTouchListener {
            var orgX = 0
            var orgY = 0
            var offsetX = 0
            var offsetY = 0
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        clickCount++
                        if (clickCount == 1){
                            startTime = System.currentTimeMillis()
                        }
                        else if (clickCount == 2){
                            duration = System.currentTimeMillis() - startTime
                            if (duration <= 700){
                                fastForward = true
                                popupView.forWordSecTv.visibility = View.VISIBLE
                                clickCount = 0
                                duration = 0L
                            }
                            else{
                                clickCount = 1
                                startTime = System.currentTimeMillis()
                            }
                        }
                        if (popupView.forWordSecTv.isVisible){
                            delay(popupView.forWordSecTv)
                        }
                    }

                    MotionEvent.ACTION_DOWN -> {
                        orgX = event.x.toInt()
                        orgY = event.y.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        offsetX = (event.rawX - orgX).toInt()
                        offsetY = (event.rawY - orgY).toInt()
                        popupWindow!!.update(offsetX, offsetY, -1, -1, true)
                    }
                }
                return true
            }
        })
        return popupWindow!!


    }


    private fun karmaYouTubePlayerInitialization(
        youtubeVideoPlayer: YouTubePlayerView,
        videoId: String
    ) {
        this.lifecycle.addObserver(youtubeVideoPlayer)
        youtubeVideoPlayer.getPlayerUiController().showUi(true)
        youtubeVideoPlayer.addYouTubePlayerListener(object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                if (fastForward){
                    youTubePlayer.seekTo(second+10)
                    fastForward = false
                }
                if (fastBackWord){
                    youTubePlayer.seekTo(second-10)
                    fastBackWord = false
                }
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
            }

            override fun onPlaybackQualityChange(youTubePlayer: YouTubePlayer, playbackQuality: PlayerConstants.PlaybackQuality) {
            }

            override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {
            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (videoId.length > 11) {
                    playAt = videoId.replaceBefore("=", "").replace("=", "").toFloat()
                    karmaVideoId = videoId.replaceAfter("?", "").replace("?", "")
                    youTubePlayer.loadVideo(karmaVideoId, playAt)
                } else {
                    youTubePlayer.loadVideo(videoId, 0F)
                }
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                if (state.name == "ENDED") {
                    youTubePlayer.seekTo(playAt)
                    youTubePlayer.pause()
                }
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
            }

            override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {
            }

        })
    }

    fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
        (layoutParams as ViewGroup.MarginLayoutParams).let {
            left?.run { it.leftMargin = dpToPx(this) }
            top?.run { it.topMargin = dpToPx(this) }
            right?.run { it.rightMargin = dpToPx(this) }
            bottom?.run { it.bottomMargin = dpToPx(this) }
        }
    }
    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

    fun delay(dView:View){
        val handler = android.os.Handler()
        handler.postDelayed({
            dView.visibility = View.GONE
        }, 900)
    }
}