package com.zong.call.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.os.Build
import android.speech.tts.TextToSpeech
import java.util.*

class TextToSpeechUtils(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var audioManager: AudioManager? = null
    private var defaultVolume: Int = 0

    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        defaultVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
    }

    fun speak(text: String, audioType: Int, volume: Int, pitch: Float, speed: Float) {
        if (tts == null) {
            tts = TextToSpeech(context, this)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(audioType)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            tts?.setAudioAttributes(audioAttributes)
        }
        if (volume != AudioManager.ADJUST_SAME) {
            audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
        }
        tts?.setPitch(pitch)
        tts?.setSpeechRate(speed)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        tts?.shutdown()
        tts = null
        audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, defaultVolume, 0)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.getDefault()
        }
    }
}
