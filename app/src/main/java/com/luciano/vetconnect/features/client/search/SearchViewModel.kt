package com.luciano.vetconnect.features.client.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.api.ApiResult
import com.luciano.vetconnect.shared.data.models.FilterOptions
import com.luciano.vetconnect.shared.data.models.SortOption
import com.luciano.vetconnect.shared.data.models.Veterinary
import com.luciano.vetconnect.shared.data.models.VeterinaryService
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = VeterinaryRepository.getInstance()

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _filterOptions = MutableStateFlow(FilterOptions())
    val filterOptions: StateFlow<FilterOptions> = _filterOptions.asStateFlow()

    private val _sortOption = MutableStateFlow<SortOption?>(null)
    val sortOption: StateFlow<SortOption?> = _sortOption.asStateFlow()

    fun search(query: String) {
        if (query.isBlank()) {
            _searchState.value = SearchState.Initial
            return
        }

        _searchState.value = SearchState.Loading

        viewModelScope.launch {
            try {
                val veterinariesResult = repository.searchVeterinaries(
                    query = query,
                    filterOptions = _filterOptions.value
                )

                when (veterinariesResult) {
                    is ApiResult.Success -> {
                        val veterinariesWithServices = mutableListOf<VeterinaryWithServices>()

                        for (veterinary in veterinariesResult.data) {
                            when (val servicesResult = repository.getServicesForVeterinary(veterinary.id)) {
                                is ApiResult.Success -> {
                                    veterinariesWithServices.add(
                                        VeterinaryWithServices(
                                            veterinary = veterinary,
                                            services = servicesResult.data
                                        )
                                    )
                                }
                                is ApiResult.Error -> {
                                    veterinariesWithServices.add(
                                        VeterinaryWithServices(
                                            veterinary = veterinary,
                                            services = emptyList()
                                        )
                                    )
                                }
                            }
                        }

                        _searchState.value = SearchState.Success(veterinariesWithServices)
                    }
                    is ApiResult.Error -> {
                        _searchState.value = SearchState.Error(veterinariesResult.message)
                    }
                }
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun updateFilters(options: FilterOptions) {
        _filterOptions.value = options
        // Re-aplicar búsqueda con nuevos filtros
        val currentQuery = (_searchState.value as? SearchState.Success)?.results?.firstOrNull()?.veterinary?.name
        currentQuery?.let { search(it) }
    }

    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
        // Re-ordenar resultados actuales
        val currentState = _searchState.value
        if (currentState is SearchState.Success) {
            _searchState.value = SearchState.Success(sortResults(currentState.results))
        }
    }

    private fun sortResults(results: List<VeterinaryWithServices>): List<VeterinaryWithServices> {
        return when (_sortOption.value) {
            SortOption.NAME_ASC -> results.sortedBy { it.veterinary.name }
            SortOption.NAME_DESC -> results.sortedByDescending { it.veterinary.name }
            SortOption.RATING_HIGH -> results.sortedByDescending { it.veterinary.rating }
            SortOption.RATING_LOW -> results.sortedBy { it.veterinary.rating }
            SortOption.PRICE_LOW -> results.sortedBy { result ->
                result.services.minOfOrNull { it.price } ?: 0.0
            }
            SortOption.PRICE_HIGH -> results.sortedByDescending { result ->
                result.services.maxOfOrNull { it.price } ?: 0.0
            }
            null -> results
        }
    }
}

sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class Success(val results: List<VeterinaryWithServices>) : SearchState()
    data class Error(val message: String) : SearchState()
}

data class VeterinaryWithServices(
    val veterinary: Veterinary,
    val services: List<VeterinaryService>
)