package com.supaur.baseactivity.basefragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**
 * ViewPager模式下的懒加载。
 * 持懒加载的FragmentLazyPagerAdapter,只有主Fragment才会调用resume方法,
 * 需要配合BaseFragment使用
 */

open class FragmentLazyPagerAdapter(
    fragmentManager: FragmentManager,
    private val fragments: MutableList<Fragment>,
    private val titles: MutableList<String>?
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) = titles!![position]

}