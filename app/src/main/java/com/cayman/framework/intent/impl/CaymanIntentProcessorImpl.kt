package com.cayman.framework.intent.impl

import android.app.Activity
import android.content.Intent
import com.cayman.framework.intent.interfaces.CaymanIntentProcessor
import com.cayman.framework.intent.interfaces.CaymanIntentProcessor.ActivityResultObserver

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
    private val observers =
        hashMapOf<Int, MutableList<CaymanIntentProcessor.ActivityResultObserver>>()


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
        observer: CaymanIntentProcessor.ActivityResultObserver
    ) {
        removeActivityResultObserver(requestCode, observer)
        observers[requestCode]?.add(observer) ?: run {
            observers[requestCode] = mutableListOf(observer)
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
        observer: CaymanIntentProcessor.ActivityResultObserver
    ) {
        observers[requestCode]?.remove(observer)
    }

    /**
     * Clear observers list for activities results
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun clear() {
        observers.clear()
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
        observer: CaymanIntentProcessor.ActivityResultObserver?
    ) {
        observer?.let {
            addActivityResultObserver(requestCode, it)
        }
        activity.startActivityForResult(intent, requestCode)
    }

}