package com.zong.call.utils

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.zong.common.ext.buildMainHandler
import com.zong.common.ext.toastOnUi
import com.zong.call.APP
import com.zong.call.R
import com.zong.call.constant.SoundConfig
import com.zong.call.constant.audioTypeList
import splitties.init.appCtx
import java.util.*


class TTS {

    private val handler by lazy { buildMainHandler() }

    private val tag = "tts---》"

    private val clearTtsRunnable = Runnable { clearTts() }

    private var speakStateListener: SpeakStateListener? = null

    private var textToSpeech: TextToSpeech? = null

    private var text: String? = null

    private var onInit = false
    private var currentVolume = 0


    companion object {
        var instance = Holder.holder
    }

    private val audioMngHelper by lazy {
        AudioMngHelper(appCtx)
    }

    private object Holder {
        var holder = TTS()
    }

    private val initListener by lazy {
        InitListener()
    }

    private val utteranceListener by lazy {
        TTSUtteranceListener()
    }

    val isSpeaking: Boolean
        get() {
            return textToSpeech?.isSpeaking ?: false
        }

    fun getTTS(): TextToSpeech? {
        return textToSpeech
    }

    @Suppress("unused")
    fun setSpeakStateListener(speakStateListener: SpeakStateListener) {
        this.speakStateListener = speakStateListener
    }

    @Suppress("unused")
    fun removeSpeakStateListener() {
        speakStateListener = null
    }

    @Synchronized
    fun speak(text: String) {
        handler.removeCallbacks(clearTtsRunnable)
        this.text = text
        if (onInit) {
            return
        }
        if (textToSpeech == null) {
            onInit = true
            textToSpeech = TextToSpeech(appCtx, initListener)
        } else {
            addTextToSpeakList()
        }
    }

    fun stop() {
        textToSpeech?.stop()
    }

    @Synchronized
    fun clearTts() {
        textToSpeech?.let { tts ->
            tts.stop()
            tts.shutdown()
        }
        textToSpeech = null
    }

    private fun addTextToSpeakList() {
        textToSpeech?.let { tts ->
            kotlin.runCatching {
                var b = Bundle()
                b.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, audioTypeList[SoundConfig.getConfig().soundTypeIndex].type)
                var result = tts.speak("", TextToSpeech.QUEUE_FLUSH, b, null)
                if (result == TextToSpeech.ERROR) {
                    clearTts()
                    textToSpeech = TextToSpeech(appCtx, initListener)
                    return
                }
                text?.splitNotBlank("\n")?.forEachIndexed { i, s ->
                    result = tts.speak(s, TextToSpeech.QUEUE_ADD, b, tag + i)
                    if (result == TextToSpeech.ERROR) {
                        AppLog.put("tts朗读出错:$text")
                    }
                }
            }.onFailure {
                AppLog.put("tts朗读出错", it)
                APP.instance.toastOnUi(it.localizedMessage)
            }
        }
    }

    /**
     * 初始化监听
     */
    private inner class InitListener : TextToSpeech.OnInitListener {

        override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
//                val result: Int = textToSpeech?.setLanguage(Locale.CHINA) ?: -1
                currentVolume = audioMngHelper.get100CurrentVolume()

                audioMngHelper.setAudioType(audioTypeList[SoundConfig.getConfig().soundTypeIndex].type)
                audioMngHelper.setVoice100(SoundConfig.getConfig().soundVolume)

                textToSpeech?.setOnUtteranceProgressListener(utteranceListener)
                textToSpeech?.setSpeechRate(SoundConfig.getConfig().soundSpeed / 10f)
                textToSpeech?.setPitch(SoundConfig.getConfig().soundPitch / 10f)

//                textToSpeech?.engines?.forEach {
//                    LogUtils.d("TAG","引擎:"+it.label)
//                    LogUtils.d("TAG","引擎:"+it.name)
//                }
                addTextToSpeakList()
            } else {
                appCtx.toastOnUi(R.string.tts_init_failed)
            }
            onInit = false
        }

    }

    /**
     * 朗读监听
     */
    private inner class TTSUtteranceListener : UtteranceProgressListener() {

        override fun onStart(utteranceId: String?) {
            //开始朗读取消释放资源任务
            handler.removeCallbacks(clearTtsRunnable)
            speakStateListener?.onStart()
        }

        override fun onDone(utteranceId: String?) {
            //一分钟没有朗读释放资源
            handler.postDelayed(clearTtsRunnable, 60000L)
            audioMngHelper.setVoice100(currentVolume)
            speakStateListener?.onDone()
            removeSpeakStateListener()
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
            speakStateListener?.onDone()
            removeSpeakStateListener()
        }

    }

    interface SpeakStateListener {
        fun onStart()
        fun onDone()
    }
}