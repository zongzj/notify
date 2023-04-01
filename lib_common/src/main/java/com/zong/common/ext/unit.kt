package com.zong.common.ext

import android.content.res.Resources
import android.util.TypedValue
import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

val Double.dp
    get() = if(this > 0.0 && TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt() == 0) 1 else TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

val Int.dp
    get() = if(this > 0 && TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt() == 0) 1 else TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

val Int.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()


val Int.px
    get() = (this.toFloat() /(Resources.getSystem().displayMetrics.density) + 0.5f);

inline fun <reified T> String.toBeanList(): MutableList<T> =
    Gson().fromJson(this, ParameterizedTypeImpl(T::class.java))
val Int.Wrap
    get() = if (this > 9) {
        this
    } else {
        "0${this}"
    };
