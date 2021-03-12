package com.adamoi.coronannya.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.adamoi.coronannya.R
import com.adamoi.coronannya.models.GlobalSummary
import com.adamoi.coronannya.services.CovidMathroidService
import com.adamoi.coronannya.services.httpClient
import com.adamoi.coronannya.services.mathdroidApiRequest
import com.adamoi.coronannya.utils.dismissLoading
import com.adamoi.coronannya.utils.showLoading
import com.adamoi.coronannya.utils.showToast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_global_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class GlobalSummaryFragment : Fragment() {
    private lateinit var confirmed: String
    private lateinit var active: String
    private lateinit var recovered: String
    private lateinit var deaths: String
    private lateinit var lastUpdate: String
    private val httpClient = httpClient()
    private val mathdroidApiRequest = mathdroidApiRequest<CovidMathroidService>(httpClient)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_global_summary, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveSummaryData()
        swipeRefreshLayout.setOnRefreshListener {
            retrieveSummaryData()
        }
    }

    private fun retrieveSummaryData() {
        setInvisible()
        showLoading(requireContext(), swipeRefreshLayout)

        val call = mathdroidApiRequest.getGlobal()
        call.enqueue(object : Callback<GlobalSummary> {
            override fun onFailure(call: Call<GlobalSummary>, t: Throwable) {
                setVisible()
                dismissLoading(swipeRefreshLayout)
            }

            override fun onResponse(
                call: Call<GlobalSummary>, response:
                Response<GlobalSummary>
            ) {
                dismissLoading(swipeRefreshLayout)
                setVisible()
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null -> {
                                val item = response.body()!!
                                confirmed = item.confirmed.value.toString()
                                recovered = item.recovered.value.toString()
                                deaths = item.deaths.value.toString()
                                active =
                                    (confirmed.toInt() - (recovered.toInt() + deaths.toInt())).toString()
                                lastUpdate = item.lastUpdate
                                showSummaryData()
                            }
                            else ->
                                showToast(context!!, "Berhasil")
                        }
                }
            }
        })
    }

    private fun showSummaryData() {
        createGlobalSummaryChart()
        txtConfirmed.text = NumberFormat.getNumberInstance(Locale.US).format(confirmed.toInt())
        txtActive.text = NumberFormat.getNumberInstance(Locale.US).format(active.toInt())
        txtRecovered.text = NumberFormat.getNumberInstance(Locale.US).format(recovered.toInt())
        txtDeath.text = NumberFormat.getNumberInstance(Locale.US).format(deaths.toInt())
        setLastUpdate(lastUpdate)
    }

    private fun createGlobalSummaryChart() {
        val pieChart: PieChart = globalSummaryChart
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(recovered.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.colorGreen))
        listPie.add(PieEntry(active.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.colorYellow))
        listPie.add(PieEntry(deaths.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.colorRed))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors
        pieDataSet.setDrawValues(false)
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(false)
            isDrawHoleEnabled = true
            holeRadius = 60f
            transparentCircleRadius = 0f
            animateY(1400, Easing.EaseInOutQuad)
//            setHoleColor(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.colorPrimary
//                )
//            )
            invalidate()
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun setLastUpdate(lastUpdate: String) {
        val format =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        try {
            val date: Date = format.parse(lastUpdate)
            val formatter =
                SimpleDateFormat("yyyy-MM-dd | HH:mm:ss")
            val output =
                formatter.format(date)
            txtLastUpdate.text = output.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("error", e.message.toString())
        }
    }

    private fun setVisible() {
        graphCard.visibility = View.VISIBLE
        firstStatusRow.visibility = View.VISIBLE
        secondStatusRow.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setInvisible() {
        graphCard.visibility = View.GONE
        firstStatusRow.visibility = View.GONE
        secondStatusRow.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

}