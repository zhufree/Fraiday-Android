package info.free.duangjike.animation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import info.free.duangjike.PageFragment

/**
 * Created by zhufree on 2018/1/24.
 *
 */
class AnimationAdapter(supportFragmentManager: FragmentManager, val titleList: MutableList<String>, val mFragments: MutableList<PageFragment>)
    : FragmentPagerAdapter(supportFragmentManager) {
    var curFragment: PageFragment? = null

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        curFragment = `object` as PageFragment
    }
}
