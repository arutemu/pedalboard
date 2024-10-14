package com.mukuro.pedalboard.ui

import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
///
import com.mukuro.pedalboard.data.Plugin
import com.mukuro.pedalboard.data.PluginsRepository
import com.mukuro.pedalboard.data.PluginsRepositoryImpl
import com.mukuro.pedalboard.ui.utils.PedalboardContentType
import kotlinx.coroutines.Dispatchers
///
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PedalboardHomeViewModel(private val pluginsRepository: PluginsRepository = PluginsRepositoryImpl()) :
    ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(PedalboardHomeUIState(loading = true))
    val uiState: StateFlow<PedalboardHomeUIState> = _uiState
    // Palettes data
    val paletteColors: MutableLiveData<List<PaletteColors>> = MutableLiveData()

    init {
        observeBoard()
        //generatePalettes()
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
                        //openedPlugin = plugins.first()
                    )
                }
        }
    }

/*        suspend private fun generatePalettes() {
            viewModelScope.launch {
                pluginsRepository.getAllPlugins()
                    .catch { ex ->
                        _uiState.value = PedalboardHomeUIState(error = ex.message)
                    }
                    .collect { plugins ->

                        _uiState.value.copy(
                            color =
                        )
                    }
            }
        }*/

    // TODO - 24/09/24 - implement for presets and replace setOpenedPlugin + toggleSelectedPlugin
/*    fun setOpenPreset(presetId: Long, contentType: PedalboardContentType) {
        val preset = uiState.value.presets.find { it.id == presetId }
        _uiState.value = _uiState.value.copy(
            openedPreset = preset,
        )
    }*/
    fun closeQuickMenu() {
        _uiState.value = _uiState
            .value.copy(
                isDetailOnlyOpen = false,
                //openedPlugin = _uiState.value.plugins.first()
            )
    }

    // TODO - implement adding plugin to the board
    fun addPlugin(pluginId: Long) {}

    // TODO - implement removing plugin from the board
    fun removePlugin(pluginId: Long) {}

    // TODO - implement start of audio engine
    //fun startEngine() {}

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

    // TODO remove OR REFACTOR to TOOGLE PLUGIN on/off
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
                //openedPlugin = _uiState.value.plugins.first()
            )
    }
}
// TODO - mostly leftovers + placeholder, to refactor
data class PedalboardHomeUIState(
    val plugins: List<Plugin> = emptyList(),
    //val color: List<Color>? = null,
    val selectedPlugins: Set<Long> = emptySet(),
    val openedPlugin: Plugin? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)

data class PaletteColors(
    val color1: Int,
    val color2: Int,
    val color3: Int
)

