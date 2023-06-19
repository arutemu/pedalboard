package com.mukuro.pedalboard.data
 
import com.mukuro.pedalboard.data.local.LocalPluginsDataProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PluginsRepositoryImpl : PluginsRepository{

    override fun getAllPlugins(): Flow<List<Plugin>> = flow {
        emit(LocalPluginsDataProvider.allPlugins)
    }
    override fun getAllPluginType(type: PluginType): Flow<List<Plugin>> = flow {
        val typePlugins = LocalPluginsDataProvider.allPlugins.filter{ it.type == type}         // THERE MAY BE A BIG ISSUE
        emit(typePlugins) // also CHANGE THE NAME HERE
    }
    override fun getAllTypes(): List<String> {
        return LocalPluginsDataProvider.getAllTypes()
    }
    override fun getPluginById(id: Long): Flow<Plugin?> = flow {
        val typePlugins = LocalPluginsDataProvider.allPlugins.firstOrNull { it.id == id }
        // also CHANGE THE NAME HERE
    }
}