package com.cayman.framework.flow.obj

/**
 * Cayman Framework flow class.
 *
 * @param T Type of your state.
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
sealed class CaymanFlow <out T> {

    /**
     * Background process state of flow.
     *
     * @param isIndicate Enable or disable process indication
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    class Doing(val isIndicate: Boolean = false) : CaymanFlow<Nothing>()

    /**
     * Error state of flow. If you need delivery exception to fragment - use this.
     *
     * @param throwable Exception, throwable or null if not specified
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    class Throws(val throwable: Throwable? = null) : CaymanFlow<Nothing>()

    /**
     * Your state instance of flow.
     *
     * @param state State
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced.
     */
    class State <T> (val state: T) : CaymanFlow<Nothing>()

}