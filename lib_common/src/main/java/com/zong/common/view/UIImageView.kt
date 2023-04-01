package com.zong.common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.android.common.view.helper.IBadge
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget
import com.zong.common.R
import com.zong.common.ext.dp
import kotlin.math.max
import kotlin.math.min


/**
 * @author administrator
 */
class UIImageView : AppCompatImageView {

    companion object {
        const val CIRCLE = -0X0111
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val glide by lazy {
        Glide.with(this)
    }

    private var readyLoad = false

    fun loadLogo(imageUrl: String?) {
        if (readyLoad) {
            glide.clear(this)
        }
        readyLoad = true
        val options = RequestOptions()
            .placeholder(R.mipmap.default_logo)
            .error(R.mipmap.default_logo)
            .transforms(CenterCrop(), RoundedCorners(90.dp))

        glide.load(imageUrl.getLogoImageUrl())
            .apply(options)
            .into(this)

    }

    fun loadLogoFit(imageUrl: String?) {
        if (readyLoad) {
            glide.clear(this)
        }
        readyLoad = true
        val options = RequestOptions()
            .placeholder(R.mipmap.default_logo)
            .error(R.mipmap.default_logo)
            .transforms(RoundedCorners(90.dp))

        glide.load(imageUrl.getLogoImageUrl())
            .apply(options)
            .into(this)
    }

    fun loadLogoUri(uri: String) {
        setBackgroundColor(Color.WHITE)
        if (readyLoad) {
            glide.clear(this)
        }
        readyLoad = true
        val options = RequestOptions()
            //.placeholder(R.mipmap.default_logo)
            //.error(R.mipmap.default_logo)
            .transforms(CenterCrop(), RoundedCorners(90.dp))

        glide.load(uri)
            .apply(options)
            .into(this)

    }

    fun loadImage(uri: Int) {
        setBackgroundColor(Color.WHITE)
        if (readyLoad) {
            glide.clear(this)
        }
        readyLoad = true
        val options = RequestOptions()
            //.placeholder(R.mipmap.default_logo)
            //.error(R.mipmap.default_logo)
            .transforms(CenterCrop(), RoundedCorners(90.dp))

        glide.load(uri)
            .apply(options)
            .into(this)

    }

    fun loadImageAll(imageUrl: String?) {
        setBackgroundColor(Color.WHITE)
        if (readyLoad) {
            glide.clear(this)
        }

        readyLoad = true

        val options = RequestOptions()
        //.placeholder(R.mipmap.empty_image)
        //.error(R.mipmap.empty_image)

        glide.load(imageUrl.getImageUrl())
            .apply(options)
            .into(this)
    }


    fun loadImage(
        imageUrl: String?,
        radius: Int = 0
    ) {
        setBackgroundColor(Color.WHITE)
        if (readyLoad) {
            glide.clear(this)
        }

        readyLoad = true

        val options = RequestOptions()
            //.placeholder(R.mipmap.empty_image)
            //.error(R.mipmap.empty_image)
            .centerCrop()

        if (radius > 0) {
            options.transforms(CenterCrop(), RoundedCorners(radius))
        }
        glide.load(imageUrl.getImageUrl())
            .apply(options)
            .into(this)
    }
    fun loadCircleImage(
        bitmap: Bitmap?
    ) {
//        setBackgroundColor(Color.WHITE)
        if (readyLoad) {
            glide.clear(this)
        }

        readyLoad = true

        val options = RequestOptions()
            //.placeholder(R.mipmap.empty_image)
            //.error(R.mipmap.empty_image)
            .centerCrop()
            options.transforms(CircleCrop())

        glide.load(bitmap)
            .apply(options)
            .into(this)
    }

    fun loadActiveImage(
        imageUrl: String?,
        width: Int,
        radius: Int = 0
    ) {
        setBackgroundColor(Color.WHITE)
        if (readyLoad) {
            glide.clear(this)
        }

        readyLoad = true

        val options = RequestOptions()
            //.placeholder(R.mipmap.empty_image)
            //.error(R.mipmap.empty_image)
            .fitCenter()

        if (radius > 0) {
            options.transforms(CenterCrop(), RoundedCorners(radius))
        }

        glide.load(imageUrl.getImageUrl())
            .apply(options)
            .into(object : ImageViewTarget<Drawable>(this) {
                override fun setResource(resource: Drawable?) {
                    resource?.let {

                        val height = width * resource.intrinsicHeight / resource.intrinsicWidth

                        layoutParams.height = height

                        setDrawable(resource)
                    }
                }
            })

    }

    fun loadInviteImage(
        imageUrl: String?,
        width: Int
    ) {
        setBackgroundColor(Color.WHITE)
        glide.load(imageUrl.getImageUrl())
            .into(object : ImageViewTarget<Drawable>(this) {
                override fun setResource(resource: Drawable?) {
                    resource?.let {

                        val height = width * resource.intrinsicHeight / resource.intrinsicWidth

                        layoutParams.height = height

                        setDrawable(resource)
                    }
                }
            })

    }

    fun loadImageFit(
        imageUrl: String?,
        radius: Int = 0
    ) {
        setBackgroundColor(Color.WHITE)
        if (readyLoad) {
            glide.clear(this)
        }

        readyLoad = true

        val options = RequestOptions()
            //.placeholder(R.mipmap.empty_image)
            //.error(R.mipmap.empty_image)
            .fitCenter()

        if (radius > 0) {
            options.transforms(CenterCrop(), RoundedCorners(radius))
        }
        glide.load(imageUrl.getImageUrl())
            .apply(options)
            .into(this)

    }

    private val maxH by lazy {
        (ScreenUtils.getScreenWidth() - 45.dp) / 2 * 1.3
    }

    private val minH by lazy {
        (ScreenUtils.getScreenHeight()- 45.dp) / 2 * 0.6
    }

    fun loadImage(
        imageUrl: String?,
        radius: Int = 0,
        viewWidth: Int
    ) {
        setBackgroundColor(Color.WHITE)
        if (readyLoad) {
            glide.clear(this)
        }

        readyLoad = true

        val options = RequestOptions()
        //.placeholder(R.mipmap.empty_image)
        //.error(R.mipmap.empty_image)
        //.centerCrop()

        if (radius > 0) {
            //options.transforms(CenterCrop(),RoundedCorners(radius))
        }

        Glide.with(this)
            .load(imageUrl.getImageUrl())
            .apply(options)
            .into(object : ImageViewTarget<Drawable>(this) {
                override fun setResource(resource: Drawable?) {
                    resource?.let {

                        var expectHeight =
                            viewWidth * resource.intrinsicHeight / resource.intrinsicWidth

                        expectHeight = min(expectHeight, maxH.toInt())
                        expectHeight = max(expectHeight, minH.toInt())

                        layoutParams.height = expectHeight
                        setDrawable(resource)
                    }
                }
            })
    }

    private var badge: IBadge? = null

    fun setBadge(badge: IBadge) {
        this.badge = badge
        invalidate()
    }

    fun getBadge() = badge

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        badge?.apply {
            this.drawBadge(canvas, this@UIImageView)
        }
    }


    fun String?.getImageUrl(): String? {
        return if (this?.startsWith("http") == true) this else "https://ooeli.cdn.byartmatters.com/${this}"
    }

    fun String?.getLogoImageUrl(): String? {
        return if (this?.startsWith("http") == true) this else "https://ooeli.cdn.byartmatters.com/${this}"
    }

}
