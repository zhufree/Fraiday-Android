package info.free.duangjike

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by zhufree on 2018/1/23.
 *
 */

class PageFragment : Fragment() {
//    var title = ""
    private @LayoutRes var layoutRes: Int = 0
    private var animView: JikeView? = null

    companion object {
        fun newInstance(
//                @StringRes titleRes: Int,
                @LayoutRes layoutRes: Int): PageFragment {
            val fragment = PageFragment()
            val args = Bundle()
//            args.putInt("titleRes", titleRes)
            args.putInt("layoutRes", layoutRes)
            fragment.arguments = args
            return fragment
        }
    }

    fun startAnimation() {
        animView?.startAnimation()
    }

    fun saveGif() {
        animView?.createGif("test", 1080)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
//            title = getString(args.getInt("titleRes"))
            layoutRes = args.getInt("layoutRes")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(layoutRes, container, false)
        animView = view?.findViewById(R.id.anim_view)
        return view
    }
}