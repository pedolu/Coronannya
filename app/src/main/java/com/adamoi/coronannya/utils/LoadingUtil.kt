package com.adamoi.coronannya.utils

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adamoi.coronannya.R

fun showLoading(context: Context, swipeRefreshLayout: SwipeRefreshLayout) {
    swipeRefreshLayout.setColorSchemeColors(
        ContextCompat.getColor(
            context,
            R.color.blue_500
        )
    )
    swipeRefreshLayout.isRefreshing = true
}

fun dismissLoading(swipeRefreshLayout: SwipeRefreshLayout) {
    swipeRefreshLayout.isRefreshing = false
}