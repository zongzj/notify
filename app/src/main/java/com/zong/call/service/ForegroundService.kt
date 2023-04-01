package com.zong.call.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.zong.call.constant.IntentAction
import com.zong.call.service.notify.NotificationUtil
import com.zong.call.view.FloatChatView
import splitties.init.appCtx

class ForegroundService : Service() {
    private val TAG = ForegroundService::class.java.simpleName

    companion object {

        fun start(context: Context) {
            ContextCompat.startForegroundService(context, context.serviceIntent())
            context.bindToService {
                startForeground(NotificationUtil.FOREGROUND_ID, NotificationUtil.getNotification(appCtx))
            }
        }

        fun stop(context: Context) {
            context.bindToService { stopSelf() }
        }

        private fun Context.serviceIntent() = Intent(this, ForegroundService::class.java)

        private fun Context.bindToService(block: ForegroundService.() -> Unit) {
            val connection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                    block((binder as DirectBinder).service)
                    unbindService(this)
                }

                override fun onServiceDisconnected(name: ComponentName) = Unit
            }

            bindService(serviceIntent(), connection, Context.BIND_AUTO_CREATE)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")

        intent?.action?.let {
            when (it) {
                IntentAction.notify -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForeground(NotificationUtil.FOREGROUND_ID, NotificationUtil.getNotification(appCtx))
                    }
                }
                IntentAction.open_chat_view->{
//                    FloatChatView.show(appCtx)
                }
                IntentAction.close_chat_view->{
                    FloatChatView.close()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?) = DirectBinder(this)

    class DirectBinder(val service: ForegroundService) : Binder()
}