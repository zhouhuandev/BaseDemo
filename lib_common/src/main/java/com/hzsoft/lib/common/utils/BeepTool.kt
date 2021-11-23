package com.hzsoft.lib.common.utils

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.hzsoft.lib.common.R
import java.io.IOException

/**
 * 响声
 *
 * @author zhouhuan
 * @time 2021/11/23
 */
object BeepTool {
    private const val BEEP_VOLUME = 0.50f
    private const val VIBRATE_DURATION = 200
    private var playBeep = false
    private var mediaPlayer: MediaPlayer? = null

    @JvmStatic
    fun playBeep(mContext: Activity, vibrate: Boolean) {
        playBeep = true
        val audioService = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioService.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false
        }
        if (playBeep && mediaPlayer != null) {
            mediaPlayer!!.start()
        } else {
            mContext.volumeControlStream = AudioManager.STREAM_MUSIC
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer!!.setOnCompletionListener { mediaPlayer -> mediaPlayer.seekTo(0) }
            val file = mContext.resources.openRawResourceFd(R.raw.beep)
            try {
                mediaPlayer!!.setDataSource(file.fileDescriptor, file.startOffset, file.length)
                file.close()
                mediaPlayer!!.setVolume(BEEP_VOLUME, BEEP_VOLUME)
                mediaPlayer!!.prepare()
            } catch (e: IOException) {
                mediaPlayer = null
            }
        }
        if (vibrate) {
            VibrateTool.vibrateOnce(VIBRATE_DURATION)
        }
    }
}