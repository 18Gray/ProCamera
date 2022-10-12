package com.eighteengray.procamera.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eighteengray.cardlibrary.bean.BaseDataBean
import com.eighteengray.procamera.common.GenerateDataUtils
import kotlinx.coroutines.launch


class SettingsViewModel : ViewModel() {
    private val _settingsViewState = MutableLiveData<SettingsViewState>()
    val settingsViewState : LiveData<SettingsViewState> = _settingsViewState

    fun updateViewState(context: Context) {
        viewModelScope.launch {
            val settingsDataList = GenerateDataUtils.generateSettingsList(context)

            var newAlbumViewState = SettingsViewState(showPageType = ShowPageType.Normal, settingsDataList)
            _settingsViewState.postValue(newAlbumViewState)
        }
    }

}


data class SettingsViewState(
    var showPageType: ShowPageType = ShowPageType.Loading,
    var settingsList: MutableList<BaseDataBean<Settings>>,
)

sealed class ShowPageType {
    object Loading : ShowPageType()
    object Error : ShowPageType()
    object Normal : ShowPageType()
}