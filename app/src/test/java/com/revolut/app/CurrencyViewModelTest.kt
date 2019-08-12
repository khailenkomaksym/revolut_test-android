package com.revolut.app

import com.revolut.app.data.CurrencyResponse
import com.revolut.app.repository.NetworkRepository
import com.revolut.app.ui.CurrencyViewModel
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainActivityViewModelTest {

    @Mock
    private lateinit var mockRepository: NetworkRepository

    private lateinit var mainActivityViewModel: CurrencyViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainActivityViewModel = CurrencyViewModel(mockRepository)
    }

    @Test
    fun showDataFromApi() {
        Mockito.`when`(mockRepository.getCurrencyList("EUR"))
            .thenReturn(Observable.just(CurrencyResponse("EUR", "2018-09-06", HashMap())))

        val testObserver = TestObserver<CurrencyResponse>()

        mainActivityViewModel.getCurrencyList()
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValue { data -> data.base.equals("EUR") }
    }
}