package com.zong.call.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zong.call.bean.InstalledApp
import com.zong.call.utils.DateUtil

@Entity(indices = [(Index(value = ["id"], unique = true))])
class NotifyBean {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var time: Long = System.currentTimeMillis()
    var appLogo = ""
    var appName: String? = ""
    var packName: String? = ""
    var notifyTitle: String = ""
    var notifyContext: String = ""
    var currentDate: String= DateUtil.getCurrentYYDate()

    companion object {
        fun toNotifyBean(app: InstalledApp, notifyTitle: String ,notifyContext: String): NotifyBean {
            var notifyBean = NotifyBean()
            notifyBean.notifyContext = notifyContext
            notifyBean.packName = app.packageName
            notifyBean.appName = app.appName
            notifyBean.notifyTitle = notifyTitle
            notifyBean.appLogo = app.icon
            notifyBean.currentDate= DateUtil.getCurrentYYDate()
            notifyBean.time = System.currentTimeMillis()
            return notifyBean
        }
    }
}
