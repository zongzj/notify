package com.zong.call.utils


import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.IntDef
import com.zong.common.ext.toastOnUi
import com.zong.call.constant.SoundConfig
import com.zong.call.constant.audioTypeList
import com.zong.call.ext.notificationManager
import splitties.init.appCtx
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * <pre>
 * author: Chestnut
 * blog  : http://www.jianshu.com/u/a0206b5f4526
 * time  : 2017/6/17 16:11
 * desc  :  集成音量控制
 * thanks To:   http://blog.csdn.net/hufeng882412/article/details/7310131
 * dependent on:
 * update log:
</pre> *
 */
class AudioMngHelper(context: Context) {
    private val TAG = "AudioMngHelper"
    private val OpenLog = true
    private val audioManager: AudioManager
    private var NOW_AUDIO_TYPE = 3
    private var NOW_FLAG = FLAG_NOTHING
    private var VOICE_STEP_100 = 1 //0-100的步进。

    @IntDef(TYPE_MUSIC, TYPE_ALARM, TYPE_RING, TYPE_CALL, TYPE_SYSTEM)
    @Retention(RetentionPolicy.SOURCE)
    annotation class TYPE

    @IntDef(FLAG_SHOW_UI, FLAG_PLAY_SOUND, FLAG_NOTHING)
    @Retention(RetentionPolicy.SOURCE)
    annotation class FLAG

    val systemMaxVolume: Int
        get() = audioManager.getStreamMaxVolume(NOW_AUDIO_TYPE)
    val systemCurrentVolume: Int
        get() = audioManager.getStreamVolume(NOW_AUDIO_TYPE)

    /**
     * 以0-100为范围，获取当前的音量值
     * @return  获取当前的音量值
     */
    fun get100CurrentVolume(): Int {
        return 100 * systemCurrentVolume / systemMaxVolume
    }

    /**
     * 修改步进值
     * @param step  step
     * @return  this
     */
    fun setVoiceStep100(step: Int): AudioMngHelper {
        VOICE_STEP_100 = step
        return this
    }

//    /**
//     * 改变当前的模式，对全局API生效
//     * @param type
//     * @return
//     */
//    fun setAudioType(@TYPE type: Int): AudioMngHelper {
//        NOW_AUDIO_TYPE = type
//        return this
//    }

    /**
     * 改变当前的模式，对全局API生效
     * @param type
     * @return
     */
    fun setAudioType(type: Int): AudioMngHelper {
        NOW_AUDIO_TYPE = type
        return this
    }

    /**
     * 改变当前FLAG，对全局API生效
     * @param flag
     * @return
     */
    fun setFlag(@FLAG flag: Int): AudioMngHelper {
        NOW_FLAG = flag
        return this
    }

    fun addVoiceSystem(): AudioMngHelper {
        audioManager.adjustStreamVolume(NOW_AUDIO_TYPE, AudioManager.ADJUST_RAISE, NOW_FLAG)
        return this
    }

    fun subVoiceSystem(): AudioMngHelper {
        audioManager.adjustStreamVolume(NOW_AUDIO_TYPE, AudioManager.ADJUST_LOWER, NOW_FLAG)
        return this
    }

    /**
     * 调整音量，自定义
     * @param num   0-100
     * @return  改完后的音量值
     */
    fun setVoice100(num: Int): Int {
        var a = Math.ceil(num * systemMaxVolume * 0.01).toInt()
        a = if (a <= 0) 0 else a
        a = if (a >= 100) 100 else a

        try {
            audioManager.setStreamVolume(NOW_AUDIO_TYPE, a, 0)
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (get100CurrentVolume() == 0 && audioTypeList[SoundConfig.getConfig().soundTypeIndex].type== TYPE_RING
                    && !appCtx.notificationManager!!.isNotificationPolicyAccessGranted
                ) {
                    appCtx.toastOnUi("勿扰权限授权")
                    var intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    appCtx.startActivity(intent)
                }
            }
        }
        return get100CurrentVolume()
    }

    /**
     * 步进加，步进值可修改
     * 0——100
     * @return  改完后的音量值
     */
    fun addVoice100(): Int {
        var a = Math.ceil((VOICE_STEP_100 + get100CurrentVolume()) * systemMaxVolume * 0.01)
            .toInt()
        Log.d(TAG, "addVoice100: VOICE_STEP_100 = " + VOICE_STEP_100 + ",get100CurrentVolume() = " + get100CurrentVolume())
        Log.d(TAG, "addVoice100: systemMaxVolume = " + systemMaxVolume + ",a = " + a)
        a = if (a <= 0) 0 else a
        a = if (a >= 100) 100 else a
        audioManager.setStreamVolume(NOW_AUDIO_TYPE, a, NOW_FLAG)
        return get100CurrentVolume()
    }

    /**
     * 步进减，步进值可修改
     * 0——100
     * @return  改完后的音量值
     */
    fun subVoice100(): Int {
        var a = Math.floor((get100CurrentVolume() - VOICE_STEP_100) * systemMaxVolume * 0.01)
            .toInt()
        Log.d(TAG, "subVoice100: systemMaxVolume = " + systemMaxVolume + ", a = " + a)
        a = if (a <= 0) 0 else a
        a = if (a >= 100) 100 else a
        audioManager.setStreamVolume(NOW_AUDIO_TYPE, a, NOW_FLAG)
        return get100CurrentVolume()
    }

    companion object {
        /**
         * 封装：STREAM_类型
         */
        const val TYPE_MUSIC = AudioManager.STREAM_MUSIC
        const val TYPE_ALARM = AudioManager.STREAM_ALARM
        const val TYPE_RING = AudioManager.STREAM_RING
        const val TYPE_CALL = AudioManager.STREAM_VOICE_CALL
        const val TYPE_SYSTEM = AudioManager.STREAM_SYSTEM
        const val TYPE_NOTIFY = AudioManager.STREAM_NOTIFICATION

        /**
         * 封装：FLAG
         */
        const val FLAG_SHOW_UI = AudioManager.FLAG_SHOW_UI
        const val FLAG_PLAY_SOUND = AudioManager.FLAG_PLAY_SOUND
        const val FLAG_NOTHING = 0
    }

    /**
     * 初始化，获取音量管理者
     * @param context   上下文
     */
    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
}