package com.gmd.main.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.gmd.main.fragment.AFragment

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
class TabViewPagerAdapter(fm: FragmentManager, size: Int) : FragmentPagerAdapter(fm) {

    private var count: Int = 0

    init {
        this.count = size
    }

    override fun getItem(p0: Int): Fragment {
        return AFragment.newInstance(p0.toString())
    }

    override fun getCount(): Int {
        return count
    }
}