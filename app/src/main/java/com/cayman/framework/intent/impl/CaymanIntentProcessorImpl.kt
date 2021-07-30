package com.cayman.framework.intent.impl

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.cayman.framework.intent.interfaces.CaymanIntentProcessor
import com.cayman.framework.intent.interfaces.CaymanIntentProcessor.ActivityResultObserver
import com.cayman.framework.intent.interfaces.CaymanIntentProcessor.BroadcastDataObserver

/**
 * Cayman Intent processor implementation. Use for starting new activities from intent.
 * If you need simple start activity - use process intent method but if you need retrieve result
 * from activity - use process activity result
 *
 * @param activity Current activity instance
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
class CaymanIntentProcessorImpl <T : Activity> (
    private val activity: T
) : CaymanIntentProcessor {

    /**
     * Private field contains activity results observers assigned to request code
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    private val resultObservers =
        hashMapOf<Int, MutableList<ActivityResultObserver>>()

    /**
     * Private field contains broadcast receiver data observers assigned to intent filter
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    private val broadcastObservers =
        hashMapOf<IntentFilter, MutableList<BroadcastDataObserver>>()

    /**
     * Private field contains broadcast receivers
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    private val broadcastReceivers =
        hashMapOf<IntentFilter, MutableList<BroadcastReceiver>>()


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
    override fun addActivityResultObserver(
        requestCode: Int,
        observer: ActivityResultObserver
    ) {
        removeActivityResultObserver(requestCode, observer)
        resultObservers[requestCode]?.add(observer) ?: run {
            resultObservers[requestCode] = mutableListOf(observer)
        }
    }

    /**
     * Add broadcast receiver data observer
     *
     * @param intentFilter Intent filter for observer assignee
     * @param observer Broadcast data observer implementation
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun addBroadcastDataObserver(
        intentFilter: IntentFilter,
        observer: BroadcastDataObserver
    ) {
        removeBroadcastDataObserver(intentFilter, observer)
        broadcastObservers[intentFilter]?.add(observer) ?: run {
            broadcastObservers[intentFilter] = mutableListOf(observer)
        }
    }

    /**
     * Remove activity result observer
     *
     * @param requestCode Activity result request code
     * @param observer Registered activity result observer
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun removeActivityResultObserver(
        requestCode: Int,
        observer: ActivityResultObserver
    ) {
        resultObservers[requestCode]?.remove(observer)
    }

    /**
     * Remove broadcast receiver data observer
     *
     * @param intentFilter Intent filter observer assigned
     * @param observer Broadcast data observer implementation
     *
     * @author Sergey Grigorov
     * @since 1.0.0 FIrst time this was introduced
     */
    override fun removeBroadcastDataObserver(
        intentFilter: IntentFilter,
        observer: BroadcastDataObserver
    ) {
        broadcastObservers[intentFilter]?.remove(observer)
    }

    /**
     * Unregister broadcast receivers and remove all observers for theirs by intent filter
     *
     * @param intentFilter Intent filter observers and receiver assigned
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun removeBroadcastReceivers(intentFilter: IntentFilter) {
        broadcastReceivers[intentFilter]?.forEach {
            activity.unregisterReceiver(it)
        }
        broadcastReceivers[intentFilter]?.clear()
    }

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
    override fun removeBroadcastReceiver(intentFilter: IntentFilter, index: Int) {
        broadcastReceivers[intentFilter]?.get(index)?.let {
            activity.unregisterReceiver(it)
        }
        broadcastReceivers[intentFilter]?.removeAt(index)
    }

    /**
     * Clear observers list for activities results, unregister broadcast receivers and clear broadcast
     *      receivers data observers
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun clear() {
        resultObservers.clear()
        broadcastReceivers.forEach { (_, u) ->
            u.forEach {
                activity.unregisterReceiver(it)
            }
        }
        broadcastReceivers.clear()
        broadcastObservers.clear()
    }

    /**
     * Process intent. Simple starting activity with declared intent
     *
     * @param intent Intent for activity starting
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun processIntent(intent: Intent) {
        activity.startActivity(intent)
    }

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
    override fun processActivityResult(
        requestCode: Int,
        intent: Intent,
        observer: ActivityResultObserver?
    ) {
        observer?.let {
            addActivityResultObserver(requestCode, it)
        }
        activity.startActivityForResult(intent, requestCode)
    }

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
    override fun processBroadcastReceiver(
        intentFilter: IntentFilter,
        observer: BroadcastDataObserver?
    ): Int? {
        observer?.let {
            addBroadcastDataObserver(intentFilter, observer)
        }

        val receiver = object:BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                broadcastObservers[intentFilter]?.forEach {
                    it.onReceived(intent)
                }
            }
        }

        broadcastReceivers[intentFilter]?.add(receiver) ?: run {
            broadcastReceivers[intentFilter] = mutableListOf(receiver)
        }

        val index = broadcastReceivers[intentFilter]?.indexOf(receiver)
        activity.registerReceiver(receiver, intentFilter)
        return index
    }

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
    override fun notifyActivityResult(data: Intent?, requestCode: Int, resultCode: Int) {
        val isSuccess = resultCode == Activity.RESULT_OK
        val isCancelled = resultCode == Activity.RESULT_CANCELED

        resultObservers[requestCode]?.forEach {
            when (true) {
                isSuccess -> it.onSuccess(data)
                isCancelled -> it.onCancel()
                else -> it.onError(resultCode)
            }
        }
        resultObservers[requestCode]?.clear()
    }

}