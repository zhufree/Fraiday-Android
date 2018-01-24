package info.free.duangjike

import android.graphics.pdf.PdfDocument
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

/**
 * Created by zhufree on 2018/1/24.
 *
 */
class AnimationAdapter : FragmentPagerAdapter {
    private var mFragments = emptyList<PageFragment>().toMutableList()
    var curFragment: PageFragment? = null

    constructor(supportFragmentManager: FragmentManager) : super(supportFragmentManager)
    constructor(supportFragmentManager: FragmentManager, fragments: MutableList<PageFragment>) : super(supportFragmentManager) {
        mFragments = fragments
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "fragment-" + position
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any?) {
        super.setPrimaryItem(container, position, `object`)
        curFragment = `object` as PageFragment
    }
}
