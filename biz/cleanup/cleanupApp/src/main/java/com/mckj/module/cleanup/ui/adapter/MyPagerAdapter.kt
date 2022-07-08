package com.mckj.module.cleanup.ui.adapter
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

/**
 * Created by lovelin on 2016/6/23.
 */
class MyPagerAdapter(fragmentManager: FragmentManager?, fragmentList: ArrayList<Fragment>) :
    FragmentPagerAdapter(fragmentManager!!) {
    private val fragmentList: List<Fragment>
    private lateinit var mCurrentFragment: Fragment

    init {
        this.fragmentList = fragmentList
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        mCurrentFragment = `object` as Fragment
        super.setPrimaryItem(container, position, `object`)
    }

    open fun getCurrentFragment(): Fragment{
        return mCurrentFragment
    }
}