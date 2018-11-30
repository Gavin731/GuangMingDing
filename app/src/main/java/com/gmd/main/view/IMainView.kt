package com.gmd.main.view

import com.gmd.common.mvp.IBaseView

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
interface IMainView : IBaseView {

    fun initNavigation()

    fun getResourcesHint(): String
}