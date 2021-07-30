package com.cayman.framework.intent.interfaces

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import com.cayman.framework.intent.impl.CaymanIntentProcessorImpl

/**
 * Cayman Intent processor abstraction. Use for starting new activities from intent.
 * If you need simple start activity - use process intent method but if you need retrieve result
 * from activity - use process activity result
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
// TODO: Add removing broadcast receiver observers via removing receiver
interface CaymanIntentProcessor {

    /**
     * Process intent. Simple starting activity with declared intent
     *
     * @param intent Intent for activity starting
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun processIntent(intent: Intent)

    /**
     * Process intent and delivery activity result to observers. If observer register is not needed,
     * you can ignore observer parameter
     *
     * @param requestCode Activity result request code
     * @param intent Intent for starting activity
     * @param observer Observer for activity result delivery or null if not needed. You can register
     *      observer later via add activity result observer method.
     *
     * @see ActivityResultObserver
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun processActivityResult(
        requestCode: Int,
        intent: Intent,
        observer: ActivityResultObserver? = null
    )

    /**
     * Register broadcast receiver and observer for her. If observer not needed just set null or ignore
     * observer parameter
     *
     * @param intentFilter Broadcast receiver intent filter
     * @param observer Observer for broadcast receiver results or null if not needed. You can register
     *      observer later via add broadcast data observer method.
     *
     * @see BroadcastDataObserver
     *
     * @return Index of broadcast receiver
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun processBroadcastReceiver(
        intentFilter: IntentFilter,
        observer: BroadcastDataObserver? = null
    ): Int?

    /**
     * Notify observers via handling activity result by request code
     *
     * @param data Resulting intent from activity
     * @param requestCode Request code
     * @param resultCode Request result (status) code
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun notifyActivityResult(
        data: Intent?,
        requestCode: Int,
        resultCode: Int
    )

    /**
     * Add activity result observer
     *
     * @param requestCode Activity result request code
     * @param observer Activity result observer. Be removed after calling all observers for current
     *      request code
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun addActivityResultObserver(requestCode: Int, observer: ActivityResultObserver)

    /**
     * Add broadcast receiver data observer
     *
     * @param intentFilter Intent filter for observer assignee
     * @param observer Broadcast data observer implementation
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun addBroadcastDataObserver(intentFilter: IntentFilter, observer: BroadcastDataObserver)

    /**
     * Remove activity result observer
     *
     * @param requestCode Activity result request code
     * @param observer Registered activity result observer
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun removeActivityResultObserver(requestCode: Int, observer: ActivityResultObserver)

    /**
     * Remove broadcast receiver data observer
     *
     * @param intentFilter Intent filter observer assigned
     * @param observer Broadcast data observer implementation
     *
     * @author Sergey Grigorov
     * @since 1.0.0 FIrst time this was introduced
     */
    fun removeBroadcastDataObserver(intentFilter: IntentFilter, observer: BroadcastDataObserver)

    /**
     * Unregister broadcast receivers and remove all observers for theirs by intent filter
     *
     * @param intentFilter Intent filter observers and receiver assigned
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun removeBroadcastReceivers(intentFilter: IntentFilter)

    /**
     * Unregister specified broadcast receiver and remove all observers for theirs by intent filter
     * and index
     *
     * @param intentFilter Intent filter observers and receiver assigned
     * @param index Index of broadcast receiver returned by process method
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun removeBroadcastReceiver(intentFilter: IntentFilter, index: Int)

    /**
     * Clear observers list for activities results, unregister broadcast receivers and clear broadcast
     *      receivers data observers
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun clear()


    /**
     * Activity result observer interface
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    interface ActivityResultObserver {

        /**
         * Called if activity result code is RESULT_OK
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun onSuccess(data: Intent?)

        /**
         * Call if activity result code is RESULT_CANCEL
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun onCancel()

        /**
         * Call if activity result code is not in RESULT_OK or RESULT_CANCEL
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun onError(resultCode: Int)

    }


    /**
     * Broadcast receiver data observer interface
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    interface BroadcastDataObserver {

        /**
         * Method called if receiver notified about changes
         *
         * @param data Broadcast receiver data
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun onReceived(data: Intent?)

    }


    /**
     * Companion contains instance accessing methods
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    companion object {

        /**
         * Private field contains current instance or null if not created
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        private var instance: CaymanIntentProcessor? = null


        /**
         * Create new instance and destroy current
         *
         * @param from Current activity
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun <T : Activity> create(from: T) {
            destroy()
            instance = CaymanIntentProcessorImpl(from)
        }

        /**
         * Destroy current instance and clear current instance observers for exclude memory leaks
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun destroy() {
            get()?.clear()
            instance = null
        }

        /**
         * Get current Cayman Intent Processor instance or null if not created
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun get(): CaymanIntentProcessor? = instance

        /**
         * Execute inline function (functional programming) required non-null Cayman Intent Processor
         *
         * @param fn Inline function required non-null Cayman Intent Processor
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun notNull(fn: (CaymanIntentProcessor) -> Unit) = get()?.let(fn)

    }

}