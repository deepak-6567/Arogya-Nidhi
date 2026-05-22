package com.example.arogyanidhi.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arogyanidhi.data.local.FormDataEntity
import com.example.arogyanidhi.domain.model.UserProfile
import com.example.arogyanidhi.domain.repository.AuthRepository
import com.example.arogyanidhi.domain.repository.FormDataRepository
import com.example.arogyanidhi.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val formDataRepository: FormDataRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _dashboardActions = MutableStateFlow<List<DashboardAction>>(
        listOf(
            DashboardAction.Hospitals,
            DashboardAction.Schemes,
            DashboardAction.Eligibility,
            DashboardAction.AiChat,
            DashboardAction.Pharmacy,
            DashboardAction.Ambulance,
            DashboardAction.BloodBank,
            DashboardAction.TeleConsult
        )
    )
    val dashboardActions: StateFlow<List<DashboardAction>> = _dashboardActions.asStateFlow()

    private val _quickMenuActions = MutableStateFlow<List<QuickMenuAction>>(
        listOf(
            QuickMenuAction.Settings,
            QuickMenuAction.Notifications,
            QuickMenuAction.History,
            QuickMenuAction.Share,
            QuickMenuAction.Help,
            QuickMenuAction.About,
            QuickMenuAction.Logout
        )
    )
    val quickMenuActions: StateFlow<List<QuickMenuAction>> = _quickMenuActions.asStateFlow()

    private val _selectedAction = MutableStateFlow<DashboardAction?>(null)
    val selectedAction: StateFlow<DashboardAction?> = _selectedAction.asStateFlow()

    private val _selectedQuickAction = MutableStateFlow<QuickMenuAction?>(null)
    val selectedQuickAction: StateFlow<QuickMenuAction?> = _selectedQuickAction.asStateFlow()

    val allSubmittedData: StateFlow<List<FormDataEntity>> = formDataRepository.getAllFormData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectAction(action: DashboardAction?) {
        _selectedAction.value = action
    }

    fun selectQuickAction(action: QuickMenuAction?) {
        _selectedQuickAction.value = action
    }

    fun submitDashboardForm(action: DashboardAction, data: Map<String, String>) {
        viewModelScope.launch {
            formDataRepository.saveFormData(action.title, data)
            _selectedAction.value = null
        }
    }

    fun submitQuickMenuForm(action: QuickMenuAction, data: Map<String, String>) {
        viewModelScope.launch {
            formDataRepository.saveFormData(action.title, data)
            _selectedQuickAction.value = null
        }
    }

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                user?.let {
                    userRepository.getUserProfile(it.uid).collect { profile ->
                        _userProfile.value = profile
                    }
                }
            }
        }
    }
}
