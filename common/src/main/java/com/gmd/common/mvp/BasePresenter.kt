package com.gmd.common.mvp

import java.lang.ref.WeakReference

/**
 * @author: zenglinggui
 *
 * @description TODO
 *
 * @Modification History:
 *
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2018/11/30     zenglinggui       v1.0.0        create
 *
 **/
open abstract class BasePresenter<V : IBaseView> : IPresenter<V> {

    var viewRef: WeakReference<V>? = null

    abstract fun initView()

    override fun setView(view: V) {
        this.viewRef = WeakReference(view)
    }

    fun getView(): V? {
        return if (viewRef == null) {
            null
        } else {
            viewRef!!.get()
        }

    }

    fun isViewInit(): Boolean {
        return viewRef != null && viewRef!!.get() != null
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
        viewRef!!.clear()
        viewRef = null
    }
}