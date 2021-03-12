package com.adamoi.coronannya

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.adamoi.coronannya.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val menuText = arrayOf("Global", "Lokal", "Indonesia", "Berita")
    private val menuIcon = arrayOf(
        R.drawable.ic_global, R.drawable.ic_local,
        R.drawable.ic_home, R.drawable.ic_article
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false
        TabLayoutMediator(tabsLayout, viewPager
        ) { tab, position ->
            tab.text = menuText[position]
            tab.icon = ResourcesCompat.getDrawable(
                resources,
                menuIcon[position], null
            )
        }.attach()
        tabsLayout.getTabAt(0)?.icon?.setColorFilter(
            ContextCompat.getColor(applicationContext, R.color.blue_500),
            PorterDuff.Mode.SRC_IN
        )
        tabsLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.setColorFilter(
                    ContextCompat.getColor(applicationContext, R.color.blue_500),
                    PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.setColorFilter(
                    ContextCompat.getColor(applicationContext, R.color.gray),
                    PorterDuff.Mode.SRC_IN
                )   }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}