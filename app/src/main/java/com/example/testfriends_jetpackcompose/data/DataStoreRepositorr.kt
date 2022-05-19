package com.example.testfriends_jetpackcompose.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "on_boarding_pref")

class DataStoreRepository(context: Context) {
    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
        val userInfo = stringPreferencesKey(name = "userInfo")
        val dynamicLink = stringPreferencesKey(name = "link")
    }

    private val dataStore = context.dataStore

    suspend fun saveUser(user: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userInfo] = user
        }
    }

    suspend fun saveDynamicLink(link: String) {
        dataStore.edit { pre ->
            pre[PreferencesKey.dynamicLink] = link
        }
    }

    fun getDynamicLink(): Flow<String> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                //throw exception
            }
        }.map {
            val dynamicLink = it[PreferencesKey.dynamicLink] ?: ""
            dynamicLink
        }
    }

    fun readUserInfo(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val user = preferences[PreferencesKey.userInfo] ?: ""
                user
            }
    }
}