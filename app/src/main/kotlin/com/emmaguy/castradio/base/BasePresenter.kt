package com.emmaguy.castradio.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<in V : BasePresenter.View> {
    private val disposables: CompositeDisposable = CompositeDisposable()
    private var view: View? = null

    open fun onAttachView(view: V) {
        if (this.view !== null) {
            throw IllegalStateException("View " + this.view + " has already been attached")
        }

        this.view = view
    }

    open fun onDetachView() {
        if (this.view == null) {
            throw IllegalStateException("View has already been detached")
        }

        this.view = null
        this.disposables.clear()
    }

    protected fun unsubscribeOnDetach(disposable: Disposable) {
        disposables.add(disposable)
    }

    interface View {
    }
}