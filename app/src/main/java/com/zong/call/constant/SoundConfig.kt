package com.zong.call.constant

import com.blankj.utilcode.util.GsonUtils
import com.zong.call.utils.AudioMngHelper
import com.zong.call.utils.MMKVUtils

//soundVolume: Int = 1 跟随系统
//val backgroundMusic: Int = 0, 背景音乐
// val preMusic: String = "" 前奏提示音
class SoundConfig(var soundVolume: Int = 50, var soundSpeed: Int = 10,
                  var soundTypeIndex: Int = 1, var engineType: String = "",
                  var soundPitch: Int = 10,
                  var backgroundMusic: Int = 0, var preMusicPath: String = "") {

    companion object {
        fun getConfig(): SoundConfig {
            if (MMKVUtils.SoundConfig.isEmpty()) {
                MMKVUtils.SoundConfig = GsonUtils.toJson(SoundConfig())
            }
            return GsonUtils.fromJson(MMKVUtils.SoundConfig, SoundConfig::class.java)
        }

        fun setVolume(soundVolume: Int) {
            var config = getConfig()
            config.soundVolume = soundVolume
            MMKVUtils.SoundConfig = GsonUtils.toJson(config)
        }

        fun setSoundSpeed(soundSpeed: Int) {
            var config = getConfig()
            config.soundSpeed = soundSpeed
            MMKVUtils.SoundConfig = GsonUtils.toJson(config)
        }

        fun setSoundTypeIndex(soundTypeIndex: Int) {
            var config = getConfig()
            config.soundTypeIndex =soundTypeIndex
            MMKVUtils.SoundConfig = GsonUtils.toJson(config)
        }

        fun setEngineType(engineType: String) {
            var config = getConfig()
            config.engineType = engineType
            MMKVUtils.SoundConfig = GsonUtils.toJson(config)
        }

        fun setBackgroundMusic(backgroundMusic: Int) {
            var config = getConfig()
            config.backgroundMusic = backgroundMusic
            MMKVUtils.SoundConfig = GsonUtils.toJson(config)
        }

        fun setSoundPitch(soundPitch: Int) {
            var config = getConfig()
            config.soundPitch = soundPitch
            MMKVUtils.SoundConfig = GsonUtils.toJson(config)
        }

        fun setPreMusic(preMusicPath: String) {
            var config = getConfig()
            config.preMusicPath = preMusicPath
            MMKVUtils.SoundConfig = GsonUtils.toJson(config)
        }

    }
}
val audioTypeList=arrayListOf(SoundType(AudioMngHelper.TYPE_CALL,"通话"),
    SoundType(AudioMngHelper.TYPE_NOTIFY,"通知"),
    SoundType(AudioMngHelper.TYPE_RING,"铃声（外放）"),
    SoundType(AudioMngHelper.TYPE_MUSIC,"媒体"),
    SoundType(AudioMngHelper.TYPE_ALARM,"闹钟（外放）")
)
class SoundType(val type:Int,val name:String)
