package com.cayman.framework.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Helper for fast mutable live data creations.
 *
 * @param T Type of mutable live data value
 * @param source Source for mutable live data or null (null by default)
 *
 * @see MutableLiveData
 *
 * @return Mutable live data instance
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
fun <T> mutableLiveDataOf(source: T? = null): MutableLiveData<T> = MutableLiveData(source)

/**
 * Helper for fast live data from mutable live data creation.
 *
 * @param T Type of mutable live data value
 * @param mutableLiveData Mutable live data instance
 *
 * @see MutableLiveData
 * @see mutableLiveDataOf
 * @see LiveData
 *
 * @return Live data instance created from mutable live data
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
fun <T> liveDataOf(mutableLiveData: MutableLiveData<T>): LiveData<T> = mutableLiveData