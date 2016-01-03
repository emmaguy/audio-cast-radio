package com.emmaguy.audiocastradio.base

import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class AbstractPresenter<in V : AbstractPresenter.View> {
    private var view: View? = null
    private var subscriptions: CompositeSubscription? = null

    open fun onAttachView(view: V) {
        if (this.view !== null) {
            throw IllegalStateException("View " + this.view + " has already been attached")
        }

        this.view = view
        this.subscriptions = CompositeSubscription()
    }

    open fun onDetachView() {
        if (this.view == null) {
            throw IllegalStateException("View has already been detached")
        }

        this.view = null;
        this.subscriptions?.unsubscribe()
        this.subscriptions = null;
    }

    protected fun unsubscribeOnDetach(subscription: Subscription) {
        subscriptions!!.add(subscription)
    }

    interface View {
    }
}