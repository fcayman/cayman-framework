package com.cayman.framework.permission.interfaces

import android.app.Activity
import com.cayman.framework.permission.impl.CaymanPermissionProcessorImpl

/**
 * Cayman Permissions Processor abstraction. Use for check and request permissions in the runtime
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
interface CaymanPermissionProcessor {

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
    fun processPermissions(
        permissions: Array<out String>,
        requestCode: Int,
        observer: PermissionsObserver? = null
    )

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
    fun notifyPermissionsResult(
        permissions: Array<out String>,
        requestCode: Int,
        grantResults: IntArray
    )

    /**
     * Add permissions result observer assigned with request code
     *
     * @param requestCode Permissions request code
     * @param observer Permissions result observer
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun addPermissionsObserver(
        requestCode: Int,
        observer: PermissionsObserver
    )

    /**
     * Remove permissions result observer assigned with request code
     *
     * @param requestCode Permissions request code
     * @param observer Permissions result observer
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun removePermissionsObserver(
        requestCode: Int,
        observer: PermissionsObserver
    )

    /**
     * Clear permissions results observers list
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun clear()


    /**
     * Permissions results observer
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    interface PermissionsObserver {

        /**
         * Method called if all permissions is granted
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun onGrantedAll()

        /**
         * Method called if all permissions is denied
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun onDeniedAll()

        /**
         * Method called if permissions partially granted
         *
         * @param grantedPermissions List of granted permissions
         * @param deniedPermissions List of denied permissions
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun onPartially(grantedPermissions: List<String>, deniedPermissions: List<String>)

    }


    /**
     * Companion contains instance accessing methods
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    companion object {

        /**
         * Private field contains current Cayman Permissions Processor instance or null if not created
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        private var instance: CaymanPermissionProcessor? = null


        /**
         * Destroy current instance and create new from activity
         *
         * @param from Current activity instance
         *
         * @see CaymanPermissionProcessor.destroy
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun <T : Activity> create(from: T) {
            destroy()
            instance = CaymanPermissionProcessorImpl(from)
        }

        /**
         * Clear observers and delete current instance from variable
         *
         * @see CaymanPermissionProcessor.get
         * @see CaymanPermissionProcessor.clear
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun destroy() {
            get()?.clear()
            instance = null
        }

        /**
         * Get current permissions processor instance or null if not created
         *
         * @return Cayman Permissions Processor instance
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun get(): CaymanPermissionProcessor? = instance

        /**
         * Execute inline function (functional programming) required non-null processor instance
         *
         * @param fn Inline function required non-null Cayman Permissions Processor
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun notNUll(fn: (CaymanPermissionProcessor) -> Unit) = get()?.let(fn)

    }

}