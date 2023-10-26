package com.khedma.makhdomy

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAppLangToAr()

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHost.navController.addOnDestinationChangedListener { controller, destination, argument ->
            title =
                when (destination.id) {
                    R.id.basicDataMakhdomFragment -> "البيانات الأساسية"
                    R.id.addressMakhdomFragment -> "بيانات العنوان"
                    R.id.familyMakhdomFragment -> "بيانات الأسرة"
                    R.id.mediaAndHobbiesMakhdomFragment -> "بيانات الميديا"
                    R.id.spiritualMakhdomFragment -> "البيانات الروحية"
                    R.id.makhdomDetailsFragment -> "بيانات المخدوم"
                    R.id.makhdomReviewsFragment -> "حفظ بيانات المخدوم"
                    R.id.makhdommenListFragment -> "مخــدومي"
                    R.id.phoneDataFragment -> "بيانات التواصل"
                    else -> "البيانات الصحية"
                }
        }
    }

    private fun setAppLangToAr() {
        val locale = Locale("ar")
        Locale.setDefault(locale)
        val config: Configuration = baseContext.resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
    }

}