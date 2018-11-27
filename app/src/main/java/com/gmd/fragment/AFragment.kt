package com.gmd.fragment


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gmd.common.base.BaseFragment

class AFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val content = arguments!!.getString(ARG_C)
        val textView = TextView(context)
        textView.textSize = 30f
        textView.gravity = Gravity.CENTER
        textView.text = String.format("Test\n\n%s", content)
        textView.setBackgroundColor(-0x131314)
        return textView
    }

    companion object {
        private val ARG_C = "content"

        fun newInstance(content: String): AFragment {
            val args = Bundle()
            args.putString(ARG_C, content)
            val fragment = AFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
