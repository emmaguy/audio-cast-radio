package com.emmaguy.castradio.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<V : BasePresenter.View> : AppCompatActivity() {
    protected abstract fun getLayoutId(): Int

    protected abstract fun getPresenter(): BasePresenter<V>
    protected abstract fun getPresenterView(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())
        getPresenter().onAttachView(getPresenterView())
    }

    override fun onDestroy() {
        getPresenter().onDetachView()

        super.onDestroy()
    }
}
