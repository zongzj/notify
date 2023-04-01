package com.zong.call.receive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager

private class BecomingNoisyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            // Pause the playback
        }
    }
}


/**
 * 在开始播放时注册接收器，并在停止时取消注册接收器。如果按照本指南的说明设计应用，这些调用应显示在 onPlay() 和 onStop() 媒体会话回调
 */
//
//private val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
//private val myNoisyAudioStreamReceiver = BecomingNoisyReceiver()
//
//private val callback = object : MediaSessionCompat.Callback() {
//
//    override fun onPlay() {
//        registerReceiver(myNoisyAudioStreamReceiver, intentFilter)
//    }
//
//    override fun onStop() {
//        unregisterReceiver(myNoisyAudioStreamReceiver)
//    }
//}
//
    