package io.blackarrows.http.app.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.blackarrows.http.app.data.ImageModel
import io.blackarrows.http.app.data.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ImageUiState(
    val images: List<ImageModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ImageViewModel(
    private val repository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageUiState())
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    init {
        loadImages()
    }

    fun loadImages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getRandomImages(limit = 20)
                .onSuccess { images ->
                    Log.d("ImageViewModel", "Loaded ${images.size} images")
                    _uiState.value = _uiState.value.copy(
                        images = images,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    Log.e("ImageViewModel", "Failed to load images", error)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error occurred"
                    )
                }
        }
    }
}
