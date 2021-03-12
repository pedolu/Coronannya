package com.adamoi.coronannya.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamoi.coronannya.R
import com.adamoi.coronannya.adapters.LocalSummaryAdapter
import com.adamoi.coronannya.models.Countries
import com.adamoi.coronannya.models.CountrySummary
import com.adamoi.coronannya.models.LocalSummary
import com.adamoi.coronannya.services.CovidMathroidService
import com.adamoi.coronannya.services.httpClient
import com.adamoi.coronannya.services.mathdroidApiRequest
import com.adamoi.coronannya.utils.dismissLoading
import com.adamoi.coronannya.utils.showLoading
import com.adamoi.coronannya.utils.showToast
import kotlinx.android.synthetic.main.fragment_local_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LocalSummaryFragment : Fragment() {
    lateinit var adapter: LocalSummaryAdapter
    private lateinit var confirmed: String
    private lateinit var active: String
    private lateinit var recovered: String
    private lateinit var deaths: String
    private var countryList: MutableList<LocalSummary> = ArrayList()
    private val httpClient = httpClient()
    private val mathdroidApiRequest = mathdroidApiRequest<CovidMathroidService>(httpClient)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_local_summary, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countrySearch.findViewById<ImageView>(R.id.search_mag_icon)
            .setColorFilter(R.color.black)
        countrySearch.findViewById<ImageView>(R.id.search_close_btn)
            .setColorFilter(R.color.black)
        countrySearch.findViewById<TextView>(R.id.search_src_text).setTextColor(
            R.color.black
        )
        countrySearch.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countryRecyclerView.visibility = View.GONE
                adapter.filter.filter(newText)
                countryRecyclerView.visibility = View.VISIBLE
                return false
            }
        })

        countryRecyclerView.layoutManager = LinearLayoutManager(countryRecyclerView.context)
        countryRecyclerView.isNestedScrollingEnabled = false
        retrieveCountriesData()
        swipeRefreshLayout.setOnRefreshListener {
            retrieveCountriesData()
        }
    }

    private fun retrieveCountriesData() {
        setInvisible()
        showLoading(requireContext(), swipeRefreshLayout)
        val call = mathdroidApiRequest.getCountries()
        call.enqueue(object : Callback<Countries> {
            override fun onFailure(call: Call<Countries>, t: Throwable) {
                setVisible()
                dismissLoading(swipeRefreshLayout)
            }

            override fun onResponse(
                call: Call<Countries>, response:
                Response<Countries>
            ) {
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null -> {
                                val item = response.body()!!
                                for (country in item.countries) {
                                    retrieveSummaryData(country.name);
                                }
                                showSummaryData()
                            }
                            else ->
                                showToast(context!!, "Berhasil")
                        }
                }
            }
        })
    }

    private fun retrieveSummaryData(country: String) {
        val call = mathdroidApiRequest.getCountry(country)
        call.enqueue(object : Callback<CountrySummary> {
            override fun onFailure(call: Call<CountrySummary>, t: Throwable) {
//                setVisible()
//                dismissLoading(swipeRefreshLayout)
            }

            override fun onResponse(
                call: Call<CountrySummary>, response:
                Response<CountrySummary>
            ) {
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

                                countryList.add(
                                    LocalSummary(
                                        confirmed,
                                        recovered,
                                        deaths,
                                        active,
                                        item.lastUpdate,
                                        country
                                    )
                                )
                            }
                            else ->
                                showToast(context!!, "Berhasil")
                        }
                }
            }
        })
    }

    private fun showSummaryData() {
        adapter = LocalSummaryAdapter(requireContext(), countryList)
        countryRecyclerView.adapter = adapter
        dismissLoading(swipeRefreshLayout)
        setVisible()
    }


    private fun setVisible() {
        countryRecyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setInvisible() {
        countryRecyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

}