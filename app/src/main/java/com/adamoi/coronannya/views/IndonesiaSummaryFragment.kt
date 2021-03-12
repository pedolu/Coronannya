package com.adamoi.coronannya.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamoi.coronannya.R
import com.adamoi.coronannya.adapters.IndonesiaSummaryAdapter
import com.adamoi.coronannya.models.IndonesiaSummary
import com.adamoi.coronannya.models.IndonesiaSummaryData
import com.adamoi.coronannya.services.CovidMathroidService
import com.adamoi.coronannya.services.httpClient
import com.adamoi.coronannya.services.mathdroidIndonesiaApiRequest
import com.adamoi.coronannya.utils.dismissLoading
import com.adamoi.coronannya.utils.showLoading
import com.adamoi.coronannya.utils.showToast
import kotlinx.android.synthetic.*

import kotlinx.android.synthetic.main.fragment_indonesia_summary.*
import kotlinx.android.synthetic.main.fragment_indonesia_summary.progressBar
import kotlinx.android.synthetic.main.fragment_indonesia_summary.swipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IndonesiaSummaryFragment : Fragment() {
    private val httpClient = httpClient()
    private val mathdroidApiRequest = mathdroidIndonesiaApiRequest<CovidMathroidService>(httpClient)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_indonesia_summary, container, false)
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
        val call = mathdroidApiRequest.getProvinsi()
        call.enqueue(object : Callback<IndonesiaSummaryData> {
            override fun onFailure(call: Call<IndonesiaSummaryData>, t: Throwable) {
                dismissLoading(swipeRefreshLayout)
                setVisible()
            }

            override fun onResponse(
                call: Call<IndonesiaSummaryData>, response:
                Response<IndonesiaSummaryData>
            ) {
                setVisible()
                dismissLoading(swipeRefreshLayout)
                when {
                    response.isSuccessful -> {
                        when {
                            response.body() != null -> {
                                val item = response.body()!!
                                showIndonesiaSummary(item.data)
                            }
                            else -> {
                                showToast(requireContext(), "Berhasil")
                            }
                        }
                    }
                }
            }
        })
    }

    private fun showIndonesiaSummary(provinsiList: List<IndonesiaSummary>) {
        listProvinsiIndonesia.layoutManager = LinearLayoutManager(context)
        listProvinsiIndonesia.adapter =
            IndonesiaSummaryAdapter(
                requireContext(),
                provinsiList
            ) {
                val provinsi = it
                showToast(requireContext(), provinsi.provinsi)
            }
    }

    private fun setVisible() {
        txtIndonesia.visibility = View.VISIBLE
        listProvinsiIndonesia.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setInvisible() {
        txtIndonesia.visibility = View.GONE
        listProvinsiIndonesia.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}
