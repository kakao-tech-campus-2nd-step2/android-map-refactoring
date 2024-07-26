package campus.tech.kakao.map.model.state

sealed class UiState {
    data object NotInitialized : UiState()
    data object Success : UiState()
    data object Loading : UiState()
    data class Error(val e: Exception) : UiState()
}
