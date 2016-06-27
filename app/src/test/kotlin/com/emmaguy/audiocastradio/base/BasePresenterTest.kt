package com.emmaguy.audiocastradio.base

import android.support.annotation.CallSuper
import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

abstract class BasePresenterTest<out P : BasePresenter<V>, V : BasePresenter.View> {
    protected var presenter: BasePresenter<V>? = null
    private var view: V? = null

    @CallSuper @Before fun before() {
        MockitoAnnotations.initMocks(this)

        presenter = createPresenter()
        view = createView()
    }

    protected abstract fun createPresenter(): P
    protected abstract fun createView(): V

    protected fun getView(): V {
        return view!!
    }

    protected fun presenterOnAttachView() {
        presenter!!.onAttachView(view!!)
    }

    protected fun presenterOnDetachView() {
        presenter!!.onDetachView()
    }

    protected fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}