package com.eighteengray.procamera.settings

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eighteengray.cardlibrary.bean.BaseDataBean
import com.eighteengray.procamera.R
import com.eighteengray.procameralibrary.common.Constants
import com.supaur.baseactivity.baseactivity.BaseActivity
import kotlinx.android.synthetic.main.layout_common_title_recycler.*


class SettingsActivity : BaseActivity() {
    lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_common_title_recycler)
        initView()
    }

    private fun initView() {
        showProgressBar()

        settingsViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SettingsViewModel::class.java]
        settingsViewModel?.updateViewState(this)
        settingsViewModel?.settingsViewState?.observe(this, Observer {
            hideProgressBar()
            when (it.showPageType) {
                ShowPageType.Error -> showErrorView()
                ShowPageType.Normal -> showData(it.settingsList)
            }
        })

    }

    private fun showErrorView() {

    }

    private fun showData(settingsList: List<BaseDataBean<Settings>>?) {
        rl_settings.showRecyclerView(settingsList, Constants.viewModelPackage)
    }


}