package info.free.duangjike

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub


/**
 * Created by zhufree on 2018/1/23.
 *
 */

class PageFragment : Fragment() {
    var title: String? = null
    private @LayoutRes var layoutRes: Int = 0

    companion object {
        fun newInstance(@StringRes titleRes: Int, @LayoutRes layoutRes: Int): PageFragment {
            val fragment = PageFragment()
            val args = Bundle()
            args.putInt("titleRes", titleRes)
            args.putInt("layoutRes", layoutRes)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            title = getString(args.getInt("titleRes"))
            layoutRes = args.getInt("layoutRes")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater?.inflate(R.layout.fragment_page, container, false)
        val viewStub = view?.findViewById(R.id.view_stub) as ViewStub
        viewStub.layoutResource = layoutRes
        viewStub.inflate()
        return view
    }
}