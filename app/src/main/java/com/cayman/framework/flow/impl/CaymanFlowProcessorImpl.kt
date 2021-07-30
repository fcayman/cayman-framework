package com.cayman.framework.flow.impl

import androidx.lifecycle.Observer
import com.cayman.framework.flow.interfaces.CaymanFlowProcessor
import com.cayman.framework.flow.obj.CaymanFlow
import com.cayman.framework.viewmodel.CaymanVM

/**
 * Cayman Flow Processor implementation. Use for manage your states and flows
 *
 * @param T Type of your custom state
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
class CaymanFlowProcessorImpl <T1, T2 : CaymanVM<T1>> (
    private val caymanViewModel: T2
) : CaymanFlowProcessor<T1> {

    /**
     * Private field contains registered flow observers
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    private val observers = mutableListOf<(CaymanFlow<T1>) -> Unit>()

    /**
     * Private field contains live data Cayman View Model flow field observer
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    private val foreverObserver = Observer<CaymanFlow<T1>> { caymanFlow ->
        observers.forEach { observer ->
            observer(caymanFlow)
        }
    }


    /**
     * Initialization block of Cayman Flow Processor implementation. Subscribe to Cayman View Model flow field
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    init {
        caymanViewModel.caymanFlow.observeForever(foreverObserver)
    }


    /**
     * Add observer for Cayman Flow
     *
     * @param fn Inline method (function by functional programming) handled new states
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun addObserver(fn: (CaymanFlow<T1>) -> Unit) {
        observers.add(fn)
        caymanViewModel.caymanFlow.value?.let(fn)
    }

    /**
     * Remove observer their observing Cayman Flow
     *
     * @param fn Inline method (function by functional programming) handled new states. Was registered with addObserver
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun removeObserver(fn: (CaymanFlow<T1>) -> Unit) {
        observers.remove(fn)
    }

    /**
     * Clear observers list and remove observer from live data Cayman Flow field
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun removeAllObservers() {
        observers.clear()
        caymanViewModel.caymanFlow.removeObserver(foreverObserver)
    }


    /**
     * Process new Cayman Flow
     *
     * @param flow Needed Cayman Flow
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun processFlow(flow: CaymanFlow<T1>) {
        caymanViewModel.setCaymanFlow(flow)
    }

    /**
     * Process your custom state
     *
     * @param state Your custom state
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun processState(state: T1) {
        caymanViewModel.setCaymanState(state)
    }

    /**
     * Process doing Cayman Flow state
     *
     * @param isIndicate Enable or disable process indication flag
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun doing(isIndicate: Boolean) {
        caymanViewModel.setCaymanDoingState(isIndicate)
    }

    /**
     * Process error Cayman Flow state
     *
     * @param throwable Throwable, exception or null is specifying is not needed
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun throws(throwable: Throwable?) {
        caymanViewModel.setCaymanThrowable(throwable)
    }

}