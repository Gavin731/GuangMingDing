package com.gmd.main.activity

import android.os.Bundle
import com.gmd.R
import com.gmd.common.base.BaseActivity
import com.gmd.main.adapter.TabViewPagerAdapter
import com.gmd.main.presenter.MainPresenter
import com.gmd.main.view.IMainView
import kotlinx.android.synthetic.main.activity_main.*
import me.majiajie.pagerbottomtabstrip.NavigationController

/**
 * 绑定实例
 *
 * @BindView(R.id.user) EditText username;
 * @BindView(R.id.pass) EditText password;
 * @BindString(R.string.login_error) String loginErrorMessage;
 * @OnClick(R.id.submit) void submit() {
 *  }
 *
 */

class MainActivity : BaseActivity<IMainView, MainPresenter>(), IMainView {

    private val COLORS = intArrayOf(-0xbaa59c, -0xff8695, -0x86aab8, -0xa4b6b9, -0xa8400)
    private lateinit var mNavigationController: NavigationController


    override fun createPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun createView(): IMainView {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.initView()
    }

    override fun initNavigation() {
        mNavigationController = pnvTab.material()
            .addItem(R.drawable.ic_ondemand_video_black_24dp, "首页", COLORS[0])
            .addItem(R.drawable.ic_audiotrack_black_24dp, "音乐", COLORS[1])
            .addItem(R.drawable.ic_book_black_24dp, "书籍", COLORS[2])
            .addItem(R.drawable.ic_news_black_24dp, "个人", COLORS[3])
            .enableAnimateLayoutChanges()
            .build()

        val pagerAdapter = TabViewPagerAdapter(supportFragmentManager, Math.max(5, mNavigationController.itemCount))
        vpMain.adapter = pagerAdapter

        mNavigationController.setupWithViewPager(vpMain)

        //设置红掉
        mNavigationController.setMessageNumber(0, 100)
    }

    override fun getResourcesHint(): String {
        return "我是MainActivity提供的文案"
    }
}
