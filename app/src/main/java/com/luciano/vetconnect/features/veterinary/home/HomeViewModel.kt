package com.luciano.vetconnect.features.veterinary.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.models.vetinfobyid.VetInfobyIdResponse
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: VeterinaryRepository
) : ViewModel() {

    private val _veterinaryInfo = MutableLiveData<VetInfobyIdResponse>()
    val veterinaryInfo: LiveData<VetInfobyIdResponse> get() = _veterinaryInfo

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchVeterinaryInfo(vetId: Long, token: String) {
        viewModelScope.launch {
            val result = repository.getVetInfoById(vetId, token)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _veterinaryInfo.value = it
                } ?: run {
                    _errorMessage.value = "Error: La información de la veterinaria es nula"
                }
            } else {
                _errorMessage.value = "Error: ${result.exceptionOrNull()?.message ?: "Error desconocido"}"
            }
        }
    }

    companion object {
        fun provideFactory(
            repository: VeterinaryRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(repository) as T
            }
        }
    }
}