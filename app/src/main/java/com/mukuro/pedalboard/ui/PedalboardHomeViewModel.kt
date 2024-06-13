package com.mukuro.pedalboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
///
import com.mukuro.pedalboard.data.Plugin
import com.mukuro.pedalboard.data.PluginsRepository
import com.mukuro.pedalboard.data.PluginsRepositoryImpl
import com.mukuro.pedalboard.ui.utils.PedalboardContentType
///
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PedalboardHomeViewModel(private val pluginsRepository: PluginsRepository = PluginsRepositoryImpl()) :
    ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(PedalboardHomeUIState(loading = true))
    val uiState: StateFlow<PedalboardHomeUIState> = _uiState

    init {
        observeBoard()
    }

    private fun observeBoard() {
        viewModelScope.launch {
            pluginsRepository.getAllPlugins()
                .catch { ex ->
                    _uiState.value = PedalboardHomeUIState(error = ex.message)
                }
                .collect { plugins ->
                    // TODO refactor - no need to select first plugin as open
                    /**
                     * We set first (email) plugin selected by default for first App launch in large-screens
                     */
                    _uiState.value = PedalboardHomeUIState(
                        plugins = plugins,
                        openedPlugin = plugins.first()
                    )
                }
        }
    }
    // TODO remove
    fun setOpenedPlugin(pluginId: Long, contentType: PedalboardContentType) {
        /**
         * We only set isDetailOnlyOpen to true when it's only single pane layout
         */
        val plugin = uiState.value.plugins.find { it.id == pluginId }
        _uiState.value = _uiState.value.copy(
            openedPlugin = plugin,
            isDetailOnlyOpen = contentType == PedalboardContentType.SINGLE_PANE
        )
    }

    // TODO remove OR change
    fun toggleSelectedPlugin(pluginId: Long) {
        val currentSelection = uiState.value.selectedPlugins
        _uiState.value = _uiState.value.copy(
            selectedPlugins = if (currentSelection.contains(pluginId))
                currentSelection.minus(pluginId) else currentSelection.plus(pluginId)
        )
    }

    // TODO - remove/replace this one
    fun closeDetailScreen() {
        _uiState.value = _uiState
            .value.copy(
                isDetailOnlyOpen = false,
                openedPlugin = _uiState.value.plugins.first()
            )
    }
}

data class PedalboardHomeUIState(
    val plugins: List<Plugin> = emptyList(),
    val selectedPlugins: Set<Long> = emptySet(),
    val openedPlugin: Plugin? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)
