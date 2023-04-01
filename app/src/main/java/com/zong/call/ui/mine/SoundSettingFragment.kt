package com.zong.call.ui.mine

import android.content.Intent
import android.provider.Settings
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.zong.common.base.fragment.BaseBindingFragment
import com.zong.call.R
import com.zong.call.constant.SoundConfig
import com.zong.call.constant.audioTypeList
import com.zong.call.databinding.FragmentSoundSettingBinding
import com.zong.call.ext.BAO_HUO
import com.zong.call.ext.initToolbarClose
import com.zong.call.utils.AudioMngHelper
import com.zong.call.utils.TTS
import splitties.init.appCtx

class SoundSettingFragment : BaseBindingFragment<FragmentSoundSettingBinding>() {

    val audioMngHelper by lazy {
        AudioMngHelper(appCtx)
    }

    override fun viewCreated(binding: FragmentSoundSettingBinding) {
        binding.includeToolbar.toolbar.run {
            initToolbarClose("声音设置") {
                _mActivity.onBackPressed()
            }
            inflateMenu(R.menu.voice_setting_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.question -> {
                        start(WebViewFragment.newInstance(BAO_HUO, "保活教程"))
                    }
                }
                true
            }
        }
        binding.apply {
            initSeekBar()
            initSoundTypeDialog()
            initEngineTypeDialog()

            btTry.setOnClickListener {
                TTS.instance.clearTts()
                TTS.instance.speak("当前声音效果")
            }
        }


    }


    private fun FragmentSoundSettingBinding.initEngineTypeDialog() {
//        TTS.instance.speak("")
        val arrayEngineType = arrayListOf("跟随系统", "讯飞引擎")
//        myEngineChange.setRightText(arrayEngineType[SoundConfig.getConfig().engineType])
//        myEngineChange.setmOnLSettingItemClick {
//            MaterialDialog(_mActivity).show {
//                title(R.string.engine_type)
//                listItemsSingleChoice(items = arrayEngineType, initialSelection = SoundConfig.getConfig().engineType) { _, index, text ->
//                    SoundConfig.setEngineType(index)
//                    myEngineChange.setRightText(text.toString())
//                }
//                lifecycleOwner(this@SoundSettingFragment)
//            }
//        }
        myEngineChange.setmOnLSettingItemClick {

            try {
                val intent = Intent()
                intent.action = "com.android.settings.TTS_SETTINGS"
                startActivity(intent)
//                            startActivityForResult(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0)
            } catch (e: Exception) {
                startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                e.printStackTrace()
            }
                false
        }
    }

    private fun FragmentSoundSettingBinding.initSoundTypeDialog() {
        val arraySoundType = arrayListOf<String>()
        audioTypeList.forEach {
            arraySoundType.add(it.name)
        }
        myVoiceType.setRightText(arraySoundType[SoundConfig.getConfig().soundTypeIndex])
        myVoiceType.setmOnLSettingItemClick {
            MaterialDialog(_mActivity).show {
                title(R.string.sound_type)
                listItemsSingleChoice(items =arraySoundType, initialSelection = SoundConfig.getConfig().soundTypeIndex) { _, index, text ->
                    SoundConfig.setSoundTypeIndex(index)
                    TTS.instance.clearTts()
                    myVoiceType.setRightText(text.toString())
                }
                lifecycleOwner(this@SoundSettingFragment)
            }
        }
    }

    private fun FragmentSoundSettingBinding.initSeekBar() {
        tvCurrentVolume.text = SoundConfig.getConfig().soundVolume.toString()
        sbVolume.progress = SoundConfig.getConfig().soundVolume

        tvCurrentSpeed.text = SoundConfig.getConfig().soundSpeed.toString()
        sbSpeed.progress = SoundConfig.getConfig().soundSpeed

        tvCurrentPitch.text = SoundConfig.getConfig().soundPitch.toString()
        sbPitch.progress = SoundConfig.getConfig().soundPitch

        arrayListOf(sbVolume, sbSpeed, sbPitch).forEach {
            it.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    when (seekBar?.id) {
                        R.id.sb_volume -> {
                            SoundConfig.setVolume(progress)
                            tvCurrentVolume.text = progress.toString()
                        }
                        R.id.sb_speed -> {
                            tvCurrentSpeed.text = progress.toString()
                            SoundConfig.setSoundSpeed(progress)

                        }
                        R.id.sb_pitch -> {
                            tvCurrentPitch.text = progress.toString()
                            SoundConfig.setSoundPitch(progress)

                        }
                    }

                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    TTS.instance.clearTts()
                }
            })
        }
    }


}