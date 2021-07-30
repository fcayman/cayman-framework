package com.cayman.framework.permission.impl

import android.app.Activity
import android.content.pm.PackageManager
import com.cayman.framework.permission.interfaces.CaymanPermissionProcessor
import com.cayman.framework.permission.interfaces.CaymanPermissionProcessor.PermissionsObserver

/**
 * Cayman Permissions Processor implementation. Use for check and request permissions in the runtime
 *
 * @param activity Current activity instance
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
class CaymanPermissionProcessorImpl <T : Activity> (
    private val activity: T
) : CaymanPermissionProcessor {

    /**
     * Private field contains permissions result observers assigned with request codes
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    private val observers =
        hashMapOf<Int, MutableList<PermissionsObserver>>()


    /**
     * Add permissions result observer assigned with request code
     *
     * @param requestCode Permissions request code
     * @param observer Permissions result observer
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun addPermissionsObserver(
        requestCode: Int,
        observer: PermissionsObserver
    ) {
        removePermissionsObserver(requestCode, observer)
        observers[requestCode]?.add(observer) ?: run {
            observers[requestCode] = mutableListOf(observer)
        }
    }

    /**
     * Remove permissions result observer assigned with request code
     *
     * @param requestCode Permissions request code
     * @param observer Permissions result observer
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun removePermissionsObserver(
        requestCode: Int,
        observer: PermissionsObserver
    ) {
        observers[requestCode]?.remove(observer)
    }

    /**
     * Clear permissions results observers list
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun clear() {
        observers.clear()
    }

    /**
     * Process permissions request. Method check permissions and request denied or unsetted and notify
     * observer if needed. If you not need a observer just set null or ignore observer parameter
     *
     * @param permissions String array or permissions
     * @param requestCode Permissions request code
     * @param observer Permissions result observer or null if not needed
     *
     * @see PermissionsObserver
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun processPermissions(
        permissions: Array<out String>,
        requestCode: Int,
        observer: PermissionsObserver?
    ) {
        observer?.let {
            addPermissionsObserver(requestCode, it)
        }

        val requestedList = mutableListOf<String>()

        permissions.forEach {
            val isGranted = activity.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
            if (!isGranted) {
                requestedList.add(it)
            }
        }

        if (requestedList.isNotEmpty())
            activity.requestPermissions(requestedList.toTypedArray(), requestCode)
        else observers[requestCode]?.forEach {
            it.onGrantedAll()
        }
    }

    /**
     * Notify permissions request result observers by request code
     *
     * @param permissions Requested permissions list
     * @param requestCode Permissions request code
     * @param grantResults Integer array contains permissions grant results
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun notifyPermissionsResult(
        permissions: Array<out String>,
        requestCode: Int,
        grantResults: IntArray
    ) {
        var isGrantedAll = true
        var isDeniedAll = true

        val grantedPermissions = mutableListOf<String>()
        val deniedPermissions = mutableListOf<String>()

        var i = 0
        permissions.forEach {
            val grantResult = grantResults[i]

            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                isDeniedAll = false
                grantedPermissions.add(it)
            } else {
                isGrantedAll = false
                deniedPermissions.add(it)
            }

            i++
        }

        observers[requestCode]?.forEach {
            when (true) {
                isGrantedAll -> it.onGrantedAll()
                isDeniedAll -> it.onDeniedAll()
                else -> it.onPartially(grantedPermissions, deniedPermissions)
            }
        }
        observers[requestCode]?.clear()
    }

}