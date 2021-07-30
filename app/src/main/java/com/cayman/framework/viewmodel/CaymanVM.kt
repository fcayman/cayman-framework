package com.cayman.framework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cayman.framework.flow.interfaces.CaymanFlowProcessor
import com.cayman.framework.flow.obj.CaymanFlow
import com.cayman.framework.helper.liveDataOf
import com.cayman.framework.helper.mutableLiveDataOf
import java.lang.NullPointerException

/**
 * Abstract Cayman view model class with Cayman flow and Cayman Fragment integration.
 *
 * @param T Type of your custom state for using with Cayman Flow.
 *
 * @see ViewModel
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
abstract class CaymanVM <T> : ViewModel() {

    /**
     *  Companion contains Cayman view model factory creator.
     *  Factory creator may be used in Cayman Fragment but Cayman Composable use instance creation.
     *
     *  @author Sergey Grigorov
     *  @since 1.0.0 First time this was introduced
     */
    companion object {

        /**
         * Cayman view model factory creating method for Lifecycle ViewModelProvider.
         *
         * @param T1 Type of your custom state for using with Cayman Flow.
         * @param T2 Type of your Cayman view model used T1 state in Cayman Flow.
         *
         * @see ViewModelProvider
         * @see ViewModelProvider.Factory
         *
         * @return Unit provides Cayman View Model new instance factory
         *
         * @suppress Suppressed unchecked cast from constructor to Cayman view model
         *
         * @author Dmitry Sbrodov
         * @since 1.0.0 First time this was introduced
         */
        fun <T1, T2 : CaymanVM<T1>> getFactory(constructor: () -> T2):
                    () -> ViewModelProvider.NewInstanceFactory {
            return {
                object : ViewModelProvider.NewInstanceFactory() {
                    @Suppress("UNCHECKED_CAST")
                    override fun <V : ViewModel> create(modelClass: Class<V>): V {
                        return constructor() as V
                    }
                }
            }
        }
    }


    /**
     * Method called with view model started. If you use Compose called manually from CaymanComposable.
     * If you use fragments called in fragment's onStart method. Overrides for view model setup.
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    protected open fun onStart() {
        CaymanFlowProcessor.create(this)
    }

    /**
     * Method called with view model stopped. if you use Compose called manually from activity onStop.
     * If you use fragment called in fragment's onStop method. Overrides for prepare view model to clear.
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced.
     */
    protected open fun onStop() {
        CaymanFlowProcessor.destroy<T>()
    }


    /**
     * Private Cayman Flow mutable live data.
     *
     * @see mutableLiveDataOf
     * @see CaymanFlow
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    private val _caymanFlow =
        mutableLiveDataOf<CaymanFlow<T>>()

    /**
     * Public Cayman Flow live data for observing from Cayman flow processor, fragments or composables.
     *
     * @see liveDataOf
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    val caymanFlow = liveDataOf(_caymanFlow)


    /**
     * Public method for setting new Cayman flow.
     *
     * @param caymanFlow Cayman Flow state
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun setCaymanFlow(caymanFlow: CaymanFlow<T>) {
        _caymanFlow.postValue(caymanFlow)
    }

    /**
     * Public method for setting new Cayman Flow user specified state.
     *
     * @param caymanState User specified state
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun setCaymanState(caymanState: T) {
        val caymanFlow = CaymanFlow.State(caymanState)
        setCaymanFlow(caymanFlow)
    }

    /**
     * Public method for setting new Cayman Flow doing state
     *
     * @param isIndicate Enable or disable doing indication flag
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun setCaymanDoingState(isIndicate: Boolean) {
        val caymanFlow = CaymanFlow.Doing(isIndicate)
        setCaymanFlow(caymanFlow)
    }

    /**
     * Public method for setting new Cayman Flow error state.
     *
     * @param throwable Exception, throwable or null is not specified.
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    fun setCaymanThrowable(throwable: Throwable?) {
        val caymanFlow = CaymanFlow.Throws(throwable)
        setCaymanFlow(caymanFlow)
    }


    /**
     * Protected method for getting current Cayman Flow Processor instance.
     *
     * @return Cayman Flow Processor instance
     *
     * @throws NullPointerException
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    @Throws(NullPointerException::class)
    protected fun getCaymanFlowProcessor(): CaymanFlowProcessor<T> = CaymanFlowProcessor.get()!!

}