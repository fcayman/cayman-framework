package com.cayman.framework.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cayman.framework.intent.interfaces.CaymanIntentProcessor

/**
 * Abstract class of activity for implement of your activities. This Cayman activity will be used
 * via fragments (no Compose)
 *
 * @author Sergey Grigorov
 * @since 1.0.0 First time this was introduced
 */
abstract class CaymanActivity : AppCompatActivity() {

    /**
     * On create method overrides for regiser all Cayman managers, processors and controllers
     *
     * @author Sergey Grigorov
     * @since 1.0.0 First time this was introduced
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CaymanIntentProcessor.create(this)
    }

    override fun onDestroy() {
        CaymanIntentProcessor.destroy()
        super.onDestroy()
    }

}