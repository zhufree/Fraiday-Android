package info.free.duangjike

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import info.free.duangjike.animation.AnimationAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var adapter: AnimationAdapter? = null
    private var fragments: MutableList<PageFragment> = emptyList<PageFragment>().toMutableList()
    private var titles: MutableList<String> = emptyList<String>().toMutableList()

    init {
        fragments.add(PageFragment.newInstance(R.layout.fragment_fall_down))
        fragments.add(PageFragment.newInstance(R.layout.fragment_flip_dot))
        fragments.add(PageFragment.newInstance(R.layout.fragment_flipboard_like))
        fragments.add(PageFragment.newInstance(R.layout.fragment_like_around))
//        fragments.add(PageFragment.newInstance(/*R.string.anim_fall,*/ R.layout.fragment_ruler))
        titles.add("Fall Down")
        titles.add("Flip")
        titles.add("FlipBoard")
        titles.add("Zan")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar?.setTitle(R.string.anim_fall)
        setSupportActionBar(toolbar)

        adapter = AnimationAdapter(supportFragmentManager, titles, fragments)
        view_pager?.adapter = adapter
        tab_layout?.setupWithViewPager(view_pager)

        play_btn.setOnClickListener { adapter?.curFragment?.startAnimation() }
        save_btn?.setOnClickListener { adapter?.curFragment?.saveGif() }
    }
}
