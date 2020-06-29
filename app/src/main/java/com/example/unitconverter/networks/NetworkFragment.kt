package com.example.unitconverter.networks

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.unitconverter.builders.add
import com.example.unitconverter.miscellaneous.isNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList

class NetworkFragment : Fragment() {

    private var callback: DownloadCallback<*>? = null
    lateinit var urls: List<String>
    private lateinit var downloadTask: DownloadTask

    companion object {
        private const val TAG = "NETWORK FRAGMENT"
        fun createFragment(
            fragmentManager: FragmentManager,
            action: NetworkFragment.() -> Unit
        ): NetworkFragment {
            return fragmentManager
                .findFragmentByTag(TAG) as? NetworkFragment
                ?: NetworkFragment().apply(action)
                    .also {
                        fragmentManager.beginTransaction().add(it, TAG).commitNow()
                    }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as DownloadCallback<*>
        Log.e("calbal", "callback")
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    /**
     * Start non-blocking execution of DownloadTask.
     */
    fun startDownload() {
        if (!::downloadTask.isInitialized ||
            downloadTask.status == AsyncTask.Status.FINISHED ||
            downloadTask.isCancelled
        ) {
            callback?.also {
                cancelDownload()
                Log.e("start", "down  ${urls.size}")
                @Suppress("UNCHECKED_CAST")
                it as DownloadCallback<String>
                downloadTask = DownloadTask(it).apply {
                    execute(*urls.toTypedArray())
                }
            }
        }
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    fun cancelDownload() {
        if (!::downloadTask.isInitialized) return
        downloadTask.cancel(true)
    }

    private class DownloadTask(private val callback: DownloadCallback<String>) :
        AsyncTask<String, Int, List<DownloadTask.Result>>() {

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the download
         * task has completed, either the result value or exception can be a non-null value.
         * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
         */
        internal data class Result(
            val url: String,
            val resultValue: String? = null,
            val exception: Exception? = null
        )

        var timeOut = 5555
        override fun onPreExecute() {
            if (callback.networkAvailable() != true) {
                // If no connectivity, cancel task and update Callback with null data.
                callback.updateFromDownload(null, null)
                cancel(true)
            }
            callback.onProgressUpdate(Statuses.CONNECTING)
        }

        override fun doInBackground(vararg params: String): List<Result> {
            val resultArray = ArrayList<Result>(params.size)
            Log.e("pre", "pre ex  ${params.map { it }}")
            if (!isCancelled && params.isNotEmpty()) {
                runBlocking {
                    params.forEachIndexed { index, it ->
                        Log.e("eac", "$index")
                        //concurrent op
                        launch {
                            Log.e("launch", "pop  ${params.size}  $index")
                            resultArray.add {
                                try {
                                    val url = URL(it)
                                    val resultString = downloadUrl(url)
                                    Log.e("were", "result $resultString")
                                    if (resultString != null) {
                                        Result(it, resultString)
                                    } else {
                                        throw IOException("No response received")
                                    }
                                } catch (e: Exception) {
                                    Result(it, exception = e)
                                }
                            }
                        }
                    }
                }
            }
            return resultArray
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        override fun onPostExecute(result: List<Result>) {
            callback.apply {
                //send errors last
                val sortedList = ArrayDeque<Result>(result.size)

                result.forEach {
                    if (it.exception.isNotNull())
                        sortedList.offerLast(it)
                    else if (it.resultValue.isNotNull())
                        sortedList.offerFirst(it)
                }
                sortedList.forEach {
                    it.exception?.also { exception ->
                        passException(it.url, exception)
                    }
                    it.resultValue?.also { resultValue ->
                        updateFromDownload(it.url, resultValue)
                    }
                }
                Log.e("sort", "$sortedList")
                finishDownloading()
            }
        }

        override fun onProgressUpdate(vararg values: Int?) {
            callback.onProgressUpdate(values[0]!!)
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */
        @Throws(IOException::class)
        private fun downloadUrl(url: URL): String? {
            var connection: HttpsURLConnection? = null
            return try {
                publishProgress(Statuses.CONNECTING)
                connection = (url.openConnection() as? HttpsURLConnection)
                connection?.run {
                    // Timeout for reading InputStream arbitrarily set to 3000ms.
                    readTimeout = 3000
                    // Timeout for connection.connect() arbitrarily set to 3000ms.
                    connectTimeout = timeOut
                    // For this use case, set HTTP method to GET.
                    requestMethod = "GET"
                    // Already true by default but setting just in case; needs to be true since this request
                    // is carrying an input (response) body.
                    doInput = true
                    addRequestProperty("Authorization", "token ${Token.token}")
                    addRequestProperty("Accept", "application/vnd.github.v3.raw")
                    // Open communications link (network traffic occurs here).
                    connect()
                    publishProgress(Statuses.CONNECT_SUCCESS)
                    if (responseCode != HttpsURLConnection.HTTP_OK)
                        throw IOException("HTTP error code: $responseCode")
                    // Retrieve the response body as an InputStream.
                    publishProgress(Statuses.GET_INPUT_STREAM_SUCCESS)
                    Log.e("end", "end")
                    inputStream?.bufferedReader()?.use { it.readText() }
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                connection?.inputStream?.close()
                connection?.disconnect()
            }
        }
    }
}