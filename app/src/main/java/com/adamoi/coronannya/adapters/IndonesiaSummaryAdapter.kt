package com.adamoi.coronannya.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adamoi.coronannya.R
import com.adamoi.coronannya.models.IndonesiaSummary
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.indonesia_summary_item.view.*
import java.text.NumberFormat
import java.util.*

class IndonesiaSummaryAdapter(
    private val context: Context,
    private val items: List<IndonesiaSummary>,
    private val listener: (IndonesiaSummary) -> Unit
) : RecyclerView.Adapter<IndonesiaSummaryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.indonesia_summary_item,
                parent, false
            )
        )

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val txtProvinsi = containerView.txtProvinsi
        private val txtRecovered = containerView.txtRecovered
        private val txtConfirmed = containerView.txtConfirmed
        private val txtDeath = containerView.txtDeath
        private val txtActive = containerView.txtActive

        fun bindItem(item: IndonesiaSummary, listener: (IndonesiaSummary) -> Unit) {
            txtProvinsi.text = item.provinsi
            txtConfirmed.text =
                NumberFormat.getNumberInstance(Locale.US).format(item.kasusPosi)
            txtRecovered.text =
                NumberFormat.getNumberInstance(Locale.US).format(item.kasusSemb)
            txtDeath.text = NumberFormat.getNumberInstance(Locale.US).format(item.kasusMeni)
            txtActive.text = NumberFormat.getNumberInstance(Locale.US)
                .format((item.kasusPosi - (item.kasusSemb + item.kasusMeni)))
            containerView.setOnClickListener { listener(item) }
        }
    }

}