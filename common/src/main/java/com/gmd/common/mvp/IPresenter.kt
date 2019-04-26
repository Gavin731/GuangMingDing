package com.gmd.common.mvp

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
open interface IPresenter<V : IBaseView> {

    fun setView(view: V)

    fun resume()

    fun pause()

    fun destroy()
}