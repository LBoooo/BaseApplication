package com.hinacle.base.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.hinacle.base.app.AppConstant
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = AppConstant.appDataStore)

object DataStoreUtils {
    private val prefScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var appDataStore: DataStore<Preferences>
    private lateinit var dsData: SharedFlow<Preferences>

    fun init(app: Context) {
        appDataStore = app.appDataStore
        dsData = appDataStore.data.shareIn(prefScope, SharingStarted.Eagerly, 1)
    }

    private fun <T> putData(key: Preferences.Key<T>, value: T?) {
        prefScope.launch {
            appDataStore.edit {
                if (value != null) it[key] = value else it.remove(key)
            }
        }
    }

    private fun <T> readNullableData(key: Preferences.Key<T>, defValue: T?): T? {
        return runBlocking(prefScope.coroutineContext) {
            dsData.map {
                it[key] ?: defValue
            }.firstOrNull()
        }
    }

    private fun <T> readNonNullData(key: Preferences.Key<T>, defValue: T): T {
        return runBlocking(prefScope.coroutineContext) {
            dsData.map {
                it[key] ?: defValue
            }.first()
        }
    }

    fun putString(key: String, value: String?) = putData(stringPreferencesKey(key), value)
    fun putStringSet(key: String, values: Set<String>?) = putData(stringSetPreferencesKey(key), values)
    fun putInt(key: String, value: Int) = putData(intPreferencesKey(key), value)
    fun putLong(key: String, value: Long) = putData(longPreferencesKey(key), value)
    fun putFloat(key: String, value: Float) = putData(floatPreferencesKey(key), value)
    fun putBoolean(key: String, value: Boolean) = putData(booleanPreferencesKey(key), value)
    fun getString(key: String, defValue: String? = null): String? = readNullableData(stringPreferencesKey(key), defValue)
    fun getStringSet(key: String, defValues: Set<String>? = null): Set<String>? = readNullableData(stringSetPreferencesKey(key), defValues)
    fun getInt(key: String, defValue: Int = 0): Int = readNonNullData(intPreferencesKey(key), defValue)
    fun getLong(key: String, defValue: Long = 0): Long = readNonNullData(longPreferencesKey(key), defValue)
    fun getFloat(key: String, defValue: Float = 0f): Float = readNonNullData(floatPreferencesKey(key), defValue)
    fun getBoolean(key: String, defValue: Boolean = false): Boolean = readNonNullData(booleanPreferencesKey(key), defValue)
}