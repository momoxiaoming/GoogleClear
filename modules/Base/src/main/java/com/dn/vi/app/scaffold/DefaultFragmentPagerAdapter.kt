package com.dn.vi.app.scaffold

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Default adapter.
 * 兼容低版本的 FragmentPagerAdapter
 *
 * Created by holmes on 3/18.
 */
abstract class DefaultFragmentPagerAdapter : FragmentStatePagerAdapter {

    constructor(fm: FragmentManager) : super(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    )

}