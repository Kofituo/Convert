package com.example.unitconverter.networks

interface DownloadCallback<T> {

    /**
     * Indicates that the callback handler needs to update its appearance or information based on
     * the result of the task. Expected to be called from the main thread.
     */
    fun updateFromDownload(url: String?, result: T?)

    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    fun networkAvailable(): Boolean?

    /**
     * Indicate to callback handler any progress update.
     * @param progressCode must be one of the constants defined in DownloadCallback.Progress.
     */
    fun onProgressUpdate(progressCode: Int) {}

    /**
     * Indicates that the download operation has finished. This method is called even if the
     * download hasn't completed successfully.
     */
    fun finishDownloading()

    fun passException(url: String?, exception: Exception)
}