package com.otuolabs.unitconverter.ads

import android.app.Activity
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.otuolabs.unitconverter.ApplicationLoader
import com.otuolabs.unitconverter.ConvertActivity
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.Utils
import com.otuolabs.unitconverter.miscellaneous.ActivityCallbacks
import com.otuolabs.unitconverter.miscellaneous.ResetAfterNGets
import com.otuolabs.unitconverter.miscellaneous.isNotNull
import com.otuolabs.unitconverter.miscellaneous.isNull
import kotlin.random.Random

/**
 * Class to provide and manage ads
 * */
object AdsManager {
    private val context get() = ApplicationLoader.applicationContext

    fun initializeAds(): AdsManager {
        MobileAds.initialize(context) {}
        return this
    }

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    fun initializeNetworkCallBack(): Boolean {
        if (networkCallback.isNull()) {
            context?.getSystemService<ConnectivityManager>()?.apply {
                networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        val looper = context?.mainLooper ?: return
                        Handler(looper).post {
                            if (interstitialAdFailedToLoad) //it was supposed to load an ad but it didn't
                                interstitialAd?.apply {
                                    val shouldReload = !isLoaded || !isLoading || !interElapsedIsLessThanOneHour
                                    // since it might get called many times
                                    if (shouldReload) loadAd(adRequest) // compulsory loading of ad
                                }
                            if (bannerAdFailedToLoad)
                                bannerAd?.apply {
                                    if (!isLoading) loadAd(adRequest)
                                }
                            if (rewardedAdFailedToLoad)
                                rewardedAd?.loadAd(adRequest, rewardedAdLoadCallback)
                        }
                    }
                }
                registerNetworkCallback(
                        NetworkRequest.Builder().build(), networkCallback ?: return@apply
                )
            }
        }
        return networkCallback.isNotNull()
    }

    private var interstitialAd: InterstitialAd? = null

    private var interstitialAdFailedToLoad
            by ResetAfterNGets.resetAfterGet(initialValue = false, resetValue = false)

    private val shouldLoad get() = Random.nextBoolean()

    private inline val adRequest get() = ConvertActivity.adRequest

    private var interstitialAdLoadedTime = -1L

    fun initializeInterstitialAd(): InterstitialAd {
        if (interstitialAd.isNull()) {
            interstitialAd = InterstitialAd(context).apply {
                //adUnitId = "ca-app-pub-4310207592097894/5534122118"
                adUnitId = "ca-app-pub-3940256099942544/1033173712"
                loadAd(adRequest) // sorry guys
                adListener = object : AdListener() {
                    override fun onAdClosed() {
                        if (!isLoaded) {
                            //may mean it was inter that was shown
                            loadAdProperly()
                        }
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError?) {
                        interstitialAdFailedToLoad = true
                    }

                    override fun onAdLoaded() {
                        interstitialAdLoadedTime = System.currentTimeMillis()
                    }
                }
            }
        }
        return interstitialAd as InterstitialAd
    }

    private val interElapsedIsLessThanOneHour
        get() =
            Utils.timeDiffFromCurrentTime(interstitialAdLoadedTime) < Utils.hoursToMilliSeconds(1)

    /**
     * Returns whether the ad was shown or not
     * */
    fun showInterstitialAd(): Boolean {
        initializeInterstitialAd().apply {
            /**
             * If it is loaded show
             * else decide whether to load for the next try
             * */
            Log.e("lo", "$isLoaded  $interElapsedIsLessThanOneHour")
            /**
             * Note: While pre-fetching ads is a great technique,
             * it's important that publishers not keep old ads around too
             * long without displaying them. Any ad objects that have been held
             * for longer than an hour without being displayed should be discarded
             * and replaced with new ads from a new request.
             * */
            //checking whether the time elapsed is less than 1 hour
            //interstitialAdWaitingTime would have been updated by them
            if (isLoaded && interElapsedIsLessThanOneHour) {
                show()
                return true
            } else loadAdProperly()
        }
        return false
    }

    private fun InterstitialAd.loadAdProperly() {
        if (shouldLoad && !isLoading) loadAd(adRequest)
    }

    private var bannerAd: AdView? = null

    /**
     * Add the banner adView To the bottom of the constraint layout
     * Destroy the view at onDestroy
     * */
    fun initializeBannerAd(constraintLayout: ConstraintLayout): AdView {
        AdView(context).apply {
            id = R.id.bannerAdView
            ConstraintLayout
                    .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .apply {
                        bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                        startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                        layoutParams = this
                    }
            adSize = AdSize.BANNER
            adUnitId = "ca-app-pub-3940256099942544/6300978111"
            loadAd(adRequest)
            adListener = object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    bannerAdFailedToLoad = true
                }
            }
            constraintLayout.addView(this)
            bannerAd = this
            return this
        }
    }

    private var bannerAdFailedToLoad
            by ResetAfterNGets.resetAfterGet(initialValue = false, resetValue = false)


    val bannerAdCallbackListener by lazy(LazyThreadSafetyMode.NONE) {
        object : ActivityCallbacks {
            override fun onDestroy() {
                /**
                 * destroy the ad to prevent it from loading unnecessary
                 * */
                bannerAd?.destroy()
                bannerAd = null
            }

            override fun onPause() {
                bannerAd?.pause()
            }

            override fun onResume() {
                bannerAd?.resume()
            }
        }
    }

    private var rewardedAd: RewardedAd? = null
    private var rewardedAdFailedToLoad
            by ResetAfterNGets.resetAfterGet(initialValue = false, resetValue = false)

    private val rewardedAdLoadCallback by lazy(LazyThreadSafetyMode.NONE) {
        object : RewardedAdLoadCallback() {
            override fun onRewardedAdFailedToLoad(loadAdError: LoadAdError?) {
                rewardedAdFailedToLoad = true
            }
        }
    }

    fun loadRewardedAd() =
            rewardedAd ?: RewardedAd(context, "ca-app-pub-3940256099942544/5224354917").apply {
                loadAd(adRequest, rewardedAdLoadCallback)
                rewardedAd = this
            }

    fun showRewardedAd(activity: Activity, rewardedAdCallback: RewardedAdCallback): Boolean {
        rewardedAd?.apply {
            val isLoaded = isLoaded
            if (isLoaded)
                show(activity, rewardedAdCallback)
            return isLoaded
        }
        return false
    }
}