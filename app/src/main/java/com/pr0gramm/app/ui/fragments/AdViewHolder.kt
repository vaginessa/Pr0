package com.pr0gramm.app.ui.fragments

import android.content.Context
import android.net.Uri
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.pr0gramm.app.R
import com.pr0gramm.app.services.config.Config
import com.pr0gramm.app.ui.AdService
import com.pr0gramm.app.util.BrowserHelper
import com.pr0gramm.app.util.dp2px
import com.trello.rxlifecycle.android.RxLifecycleAndroid
import org.slf4j.LoggerFactory
import kotlin.math.roundToInt

class AdViewHolder private constructor(val adView: AdView, itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    companion object {
        private val logger = LoggerFactory.getLogger("AdViewHolder")

        fun new(context: Context): AdViewHolder {
            val container = FrameLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
            }

            val placeholder = ImageView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        context.dp2px(70f).roundToInt())

                scaleType = ImageView.ScaleType.CENTER_CROP

                setImageDrawable(VectorDrawableCompat.create(
                        resources, R.drawable.pr0mium_ad, null))

                setOnClickListener {
                    BrowserHelper.openCustomTab(context, Uri.parse("https://pr0gramm.com/pr0mium"))
                }
            }

            container.addView(placeholder)

            val adService = context.appKodein().instance<AdService>()

            val adView = adService.newAdView(context)
            adView.adSize = AdSize(AdSize.FULL_WIDTH, 70)

            logger.info("Starting loading ad now.")

            // now load the ad and show it, once it finishes loading
            adService.load(adView, Config.AdType.FEED)
                    // .delay(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    // .compose(RxLifecycleAndroid.bindView(container))
                    .subscribe { state ->
                        if (state == AdService.AdLoadState.SUCCESS && adView.parent == null) {
                            logger.info("Ad was loaded, showing ad now.")
                            container.removeView(placeholder)
                            container.addView(adView)
                        }
                    }

            return AdViewHolder(adView, container)
        }
    }
}
