package com.revolut.app.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.revolut.app.App
import com.revolut.app.R
import com.revolut.app.data.CurrencyResponse
import com.revolut.app.data.CurrencyValue
import com.revolut.app.listener.AmountChangeListener
import com.revolut.app.listener.CurrencyItemClickListener
import com.revolut.app.repository.NetworkRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyActivity : BaseActivity(), CurrencyItemClickListener, AmountChangeListener {

    private val disposable = CompositeDisposable()

    lateinit var currencyViewModel: CurrencyViewModel

    @Inject
    lateinit var networkRepository: NetworkRepository

    lateinit var recyclerView: RecyclerView
    lateinit var textError: TextView

    lateinit var listAdapter: CurrencyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.appComponent.injectsMainActivity(this)

        currencyViewModel = ViewModelProviders.of(this, CurrencyViewModelFactory(networkRepository)).get(CurrencyViewModel::class.java)

        textError = findViewById(R.id.textError)
        recyclerView = findViewById(R.id.recyclerView)

        //set recyclerview settings
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        listAdapter = CurrencyListAdapter(this, currencyViewModel.listCurrency)
        listAdapter.amount = currencyViewModel.amount
        listAdapter.itemClickListener = this
        listAdapter.amountChangeListener = this
        recyclerView.adapter = listAdapter
    }

    override fun onStart() {
        super.onStart()

        showProgressDialog()

        //make requests every second
        disposable.add(Observable.interval(1, TimeUnit.SECONDS)
            .flatMap<CurrencyResponse> { n ->
                currencyViewModel.getCurrencyList().subscribeOn(Schedulers.io())
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //if success
                hideProgressDialog()
                recyclerView.visibility = View.VISIBLE
                textError.visibility = View.GONE
                if (currencyViewModel.listCurrency.size > 0) {
                    //update data if already exists
                    it.rates.map {
                        val index: Int = currencyViewModel.mapCurrencyPosition.get(it.key) as Int
                        currencyViewModel.listCurrency.get(index).value = it.value
                        listAdapter.notifyItemChanged(index)
                    }
                } else {
                    //add new data if not exists
                    currencyViewModel.listCurrency.add(CurrencyValue(it.base, 1.0))
                    currencyViewModel.mapCurrencyPosition.put(it.base, 0)

                    var i = 1
                    it.rates.map {
                        currencyViewModel.listCurrency.add(CurrencyValue(it.key, it.value))
                        currencyViewModel.mapCurrencyPosition.put(it.key, i)
                        i++;
                    }
                    listAdapter.notifyDataSetChanged()
                }
            }, {
                //if error
                hideProgressDialog()
                recyclerView.visibility = View.GONE
                textError.visibility = View.VISIBLE
                textError.text = it.toString()
            }))
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    override fun onClick(position: Int) {
        if (position != 0) {
            //if click not on first item

            //set new currency, update first amount and swap items
            currencyViewModel.setCurrency(position)
            currencyViewModel.setNewAmount(position)
            currencyViewModel.moveItem(position)
            listAdapter.amount = currencyViewModel.amount
            listAdapter.notifyItemMoved(position, 0)
            listAdapter.notifyItemChanged(0)
            listAdapter.notifyItemChanged(1)
            recyclerView.scrollToPosition(0)
            hideKeyboard()
        }
    }

    override fun onChange(amount: Double) {
        //update current amount
        currencyViewModel.amount = amount
    }
}
