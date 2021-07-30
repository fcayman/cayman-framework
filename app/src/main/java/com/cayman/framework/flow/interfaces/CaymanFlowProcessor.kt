package com.cayman.framework.flow.interfaces

import com.cayman.framework.flow.impl.CaymanFlowProcessorImpl
import com.cayman.framework.flow.obj.CaymanFlow
import com.cayman.framework.viewmodel.CaymanVM

/**
 * Cayman Flow Processor abstraction. Use for manage your states and flows
 *
 * @param T Type of your custom state
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
interface CaymanFlowProcessor <T> {

    /**
     * Process new Cayman Flow
     *
     * @param flow Needed Cayman Flow
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun processFlow(flow: CaymanFlow<T>)

    /**
     * Process your custom state
     *
     * @param state Your custom state
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun processState(state: T)

    /**
     * Process doing Cayman Flow state
     *
     * @param isIndicate Enable or disable process indication flag
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun doing(isIndicate: Boolean)

    /**
     * Process error Cayman Flow state
     *
     * @param throwable Throwable, exception or null is specifying is not needed
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun throws(throwable: Throwable?)

    /**
     * Add observer for Cayman Flow
     *
     * @param fn Inline method (function by functional programming) handled new states
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun addObserver(fn: (CaymanFlow<T>) -> Unit)

    /**
     * Remove observer their observing Cayman Flow
     *
     * @param fn Inline method (function by functional programming) handled new states. Was registered with addObserver
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun removeObserver(fn: (CaymanFlow<T>) -> Unit)

    /**
     * Clear observers list and remove observer from live data Cayman Flow field
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun removeAllObservers()


    /**
     * Companion contains instance accessing methods
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    companion object {

        /**
         * Private field contains current Cayman Flow Processor instance or null
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        private var instance: CaymanFlowProcessor<*>? = null


        /**
         * Create new instance from Cayman view model
         *
         * @param from Instance of Cayman View Model
         *
         * @see CaymanVM
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun <T1, T2 : CaymanVM<T1>> create(from: T2) {
            destroy<T1>()
            instance = CaymanFlowProcessorImpl(from)
        }

        /**
         * Destroy current Cayman Flow Processor instance
         *
         * @param T Type of your state using in Cayman View Model
         *
         * @see CaymanFlowProcessor.javaClass
         * @see CaymanFlowProcessor.removeAllObservers
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun <T> destroy() {
            get<T>()?.removeAllObservers()
            instance = null
        }

        /**
         * Get current instance of Cayman Flow Processor or null if not created
         *
         * @suppress UNCHECKED_CAST
         *
         * @param T Type of your state using in Cayman View Model
         *
         * @return Cayman Flow Processor instance or null
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> get(): CaymanFlowProcessor<T>? = instance as CaymanFlowProcessor<T>?

        /**
         * Execute any inline function (functional programming scope) with non-null Cayman Flow Processor instance.
         * If instance doesn't exists function do not executed
         *
         * @param fn Inline function required non-null Cayman Flow Processor instance
         *
         * @author Sergey Grigorov
         * @since 1.0.0 First time this was introduced
         */
        fun <T> notNull(fn: (CaymanFlowProcessor<T>) -> Unit) = get<T>()?.let(fn)

    }

}