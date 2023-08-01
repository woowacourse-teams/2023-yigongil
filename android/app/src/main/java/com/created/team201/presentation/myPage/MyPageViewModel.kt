package com.created.team201.presentation.myPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.model.Profile
import com.created.domain.repository.MyPageRepository
import com.created.team201.data.datasource.remote.MyPageDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.MyPageRepositoryImpl
import com.created.team201.presentation.myPage.model.ProfileUiModel
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val myPageRepository: MyPageRepository,
) : ViewModel() {
    private val _profile: MutableLiveData<ProfileUiModel> = MutableLiveData()
    val profile: LiveData<ProfileUiModel>
        get() = _profile

    init {
        updateProfile()
    }

    private fun updateProfile() {
        viewModelScope.launch {
            myPageRepository.getMyPage()
                .onSuccess {
                    _profile.value = it.toUiModel()
                }.onFailure { }
        }
    }

    private fun Profile.toUiModel(): ProfileUiModel = ProfileUiModel(
        githubId = githubId,
        id = id,
        introduction = introduction,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        successRate = successRate,
        successfulRoundCount = successfulRoundCount,
        tier = tier,
        tierProgress = tierProgress,
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MyPageViewModel(
                    myPageRepository = MyPageRepositoryImpl(
                        MyPageDataSourceImpl(NetworkServiceModule.myPageService),
                    ),
                )
            }
        }
    }
}
