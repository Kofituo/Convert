package com.otuolabs.unitconverter.billing

import android.app.Activity
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class Billing(private val activity: Activity) {
    private val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult: BillingResult, mutableList: MutableList<Purchase>? ->
            }

    private val billingClient =
            BillingClient.newBuilder(activity).enablePendingPurchases()
                    .setListener(purchasesUpdatedListener).build()
                    .apply { connectToPlayBillingService() }

    private fun connectToPlayBillingService() {
        if (!billingClient.isReady)
            startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        // The BillingClient is ready. You can query purchases here.
                    }
                }
            }
        })
    }

    suspend fun querySkuDetails() {
        val skuList = listOf("Buy Me A Coffee", "Premium Version", "Support Development Team")
        val params =
                SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    if (!skuDetailsList.isNullOrEmpty()) {
                        skuDetailsList.forEach {
                            CoroutineScope(Job() + Dispatchers.IO).launch {
                                //billingClient.
                            }
                        }
                    }
                }
            }

        }
    }

    fun launchFlow() {
        //val flowParams = BillingFlowParams.newBuilder().setSkuDetails()
    }
}