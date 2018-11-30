package com.gmd.common.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.gmd.common.mvp.IBaseView
import com.gmd.common.mvp.IPresenter

/**
 * @author: zenglinggui
 *
 * @description TODO
 *
 * @Modification History:
 *
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2018/11/27     zenglinggui       v1.0.0        create
 *
 **/
open abstract class BaseActivity<P : IPresenter<*>> : AppCompatActivity(), IBaseView {

    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
        presenter.setView(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onPause() {
        super.onPause()
        presenter.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun showToast(message: String) {
        ToastUtils.showLong(message)
    }

    abstract fun createPresenter(): P
}