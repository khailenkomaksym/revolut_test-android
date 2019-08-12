package com.revolut.app.ui

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolut.app.Constants.NETWORK.Companion.PREFIX_IMAGE_RESOURCE
import com.revolut.app.Constants.NETWORK.Companion.PREFIX_STRING_RESOURCE
import com.revolut.app.R
import com.revolut.app.data.CurrencyValue
import com.revolut.app.listener.AmountChangeListener
import com.revolut.app.listener.CurrencyItemClickListener
import kotlinx.android.synthetic.main.item_currency.view.*


class CurrencyListAdapter(private val context: Context, private val listCurrency: ArrayList<CurrencyValue>) : RecyclerView.Adapter<CurrencyListAdapter.MyViewHolder>() {

    var itemClickListener: CurrencyItemClickListener? = null
    var amountChangeListener: AmountChangeListener? = null

    var amount: Double = 1.0

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        override fun onClick(p0: View?) {
            itemClickListener?.onClick(adapterPosition)
        }

        val textTitle = view.textTitle
        val textSubtitle = view.textSubtitle
        val imageCurrency = view.imageCurrency
        val editAmount = view.editAmount
        init {
            itemView.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currency = listCurrency.get(position)
        val title = currency.title
        val value: Double = amount * currency.value!!

        val resourceImageId: Int = context.resources.getIdentifier(PREFIX_IMAGE_RESOURCE + title?.toLowerCase(), "drawable", context.packageName)
        val resourceStringId: Int = context.resources.getIdentifier(PREFIX_STRING_RESOURCE + title?.toLowerCase(), "string", context.packageName)

        holder.textTitle.setText(title)
        holder.imageCurrency.setImageResource(resourceImageId)
        holder.textSubtitle.setText(resourceStringId)

        holder.editAmount.isEnabled = position == 0

        if (!holder.editAmount.isFocused) {
            holder.editAmount.setText(value.toString())
        }

        holder.editAmount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                if (holder.editAmount.isFocused) {
                    if (charSequence.length > 0 && !TextUtils.isEmpty(charSequence)) {
                        amount = charSequence.toString().toDouble()
                    } else {
                        amount = 0.0
                    }
                    notifyItemRangeChanged(1, listCurrency.size - 1)
                    amountChangeListener?.onChange(amount)
                }
            }

        })
    }

    override fun getItemCount() = listCurrency.size
}
