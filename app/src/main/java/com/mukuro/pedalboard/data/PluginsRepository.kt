package com.mukuro.pedalboard.data

import kotlinx.coroutines.flow.Flow

interface PluginsRepository {
    // TODO - reimplement this shit, this is PLACEHOLDER for now
    fun getAllPlugins(): Flow<List<Plugin>>
    fun getAllPluginType(type: PluginType): Flow<List<Plugin>>
    fun getAllTypes(): List<String>
    fun getPluginById(id: Long): Flow<Plugin?>
}