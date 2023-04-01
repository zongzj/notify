package com.zong.call.utils

import com.zong.common.utils.MMKVUtil


object MMKVUtils {
    var switch_report_num_name: Boolean
        get() {
            return MMKVUtil.getBool("switch_report_num_name")
        }
        set(value) {
            MMKVUtil.putBool("switch_report_num_name", value)
        }
    var sw_read_screen: Boolean
        get() {
            return MMKVUtil.getBool("sw_read_screen")
        }
        set(value) {
            MMKVUtil.putBool("sw_read_screen", value)
        }
    var sw_read_chat: Boolean
        get() {
            return MMKVUtil.getBool("sw_read_chat")
        }
        set(value) {
            MMKVUtil.putBool("sw_read_chat", value)
        }
    var is_agree: Boolean
        get() {
            return MMKVUtil.getBool("is_agree")
        }
        set(value) {
            MMKVUtil.putBool("is_agree", value)
        }
  var is_first_install: Boolean
        get() {
            return MMKVUtil.getBool("is_first_install")
        }
        set(value) {
            MMKVUtil.putBool("is_first_install", value)
        }

    var function_list: String
        get() {
            return MMKVUtil.getString("function_list")
        }
        set(value) {
            MMKVUtil.putString("function_list", value)
        }
    var SoundConfig: String
        get() {
            return MMKVUtil.getString("SoundConfig")
        }
        set(value) {
            MMKVUtil.putString("SoundConfig", value)
        }


}