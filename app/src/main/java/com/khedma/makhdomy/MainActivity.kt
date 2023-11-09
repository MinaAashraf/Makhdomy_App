package com.khedma.makhdomy

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAppLangToEn()
    }

    private fun setAppLangToEn() {
        val locale = Locale("en")
        Locale.setDefault(locale)
        val config: Configuration = baseContext.resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
    }
}