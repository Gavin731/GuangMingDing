package com.gmd.main.presenter

import com.gmd.common.mvp.BasePresenter
import com.gmd.main.view.IMainView

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
class MainPresenter : BasePresenter<IMainView>() {

    override fun initView() {
        getView()!!.showToast(getView()!!.getResourcesHint())
        getView()!!.initNavigation()
    }


}