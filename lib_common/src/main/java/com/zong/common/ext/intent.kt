package com.zong.common.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import kotlin.reflect.KClass

/**
 * @author administrator
 */

fun AppCompatActivity.openActivity(
    clazz: KClass<out Activity>,
    compat: ActivityOptionsCompat? = null
) {
//    val transactionBundle =
//        compat?.toBundle() ?: ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()

    startActivity(Intent(this, clazz.java), compat?.toBundle())

}

fun Context.openActivity(
    clazz: KClass<out Activity>,
    bundle: Bundle? = null,
    compat: ActivityOptionsCompat? = null
) {
//    val transactionBundle =
//        compat?.toBundle() ?: ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity)
//            .toBundle()
    val intent = Intent(this, clazz.java)
    bundle?.apply {
        intent.putExtras(this)
    }
    startActivity(intent, compat?.toBundle())

}

