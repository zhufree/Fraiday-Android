package info.free.duangjike

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import butterknife.BindView
import butterknife.ButterKnife

class MainActivity : AppCompatActivity() {
    @BindView(R.id.tab_layout)
    lateinit var tabLayout: TabLayout
    @BindView(R.id.view_pager)
    lateinit var viewPager: ViewPager
    private var fragments: MutableList<PageFragment> = emptyList<PageFragment>().toMutableList()

    init {
        fragments.add(PageFragment.newInstance(R.string.anim_fall, R.layout.layout_fall_down_anim))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return fragments[position].toString()
            }
        }
        tabLayout.setupWithViewPager(viewPager)
    }
}
