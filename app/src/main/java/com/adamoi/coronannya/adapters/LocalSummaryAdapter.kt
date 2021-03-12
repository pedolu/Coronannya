package com.adamoi.coronannya.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.adamoi.coronannya.R
import com.adamoi.coronannya.models.LocalSummary
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.local_summary_item.view.*
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LocalSummaryAdapter(
    private val context: Context, private val items:
    List<LocalSummary>
) :
    RecyclerView.Adapter<LocalSummaryAdapter.ViewHolder>(), Filterable {
    private var countryFilterList = items


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.local_summary_item,
                parent, false
            )
        )

    override fun getItemCount() = countryFilterList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(countryFilterList[position])
    }

    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindItem(item: LocalSummary) {
            containerView.txtCountryName.text = item.country
            setLastUpdate(item.lastUpdate)
            containerView.txtConfirmed.text =
                NumberFormat.getNumberInstance(Locale.US).format(item.confirmed.toInt())
            containerView.txtActive.text =
                NumberFormat.getNumberInstance(Locale.US)
                    .format(item.confirmed.toInt() - (item.recovered.toInt() + item.deaths.toInt()))
            containerView.txtRecovered.text =
                NumberFormat.getNumberInstance(Locale.US).format(item.recovered.toInt())
            containerView.txtDeath.text =
                NumberFormat.getNumberInstance(Locale.US).format(item.deaths.toInt())
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
                containerView.txtLastUpdate.text = output
            } catch (e: ParseException) {
                e.printStackTrace()
                Log.e("error", e.message.toString())
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                countryFilterList = if (charSearch.isEmpty()) {
                    items
                } else {
                    var resultList: MutableList<LocalSummary> = ArrayList()
                    for (row in items) {
                        if (row.country.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as List<LocalSummary>
                notifyDataSetChanged()

            }

        }
    }

}