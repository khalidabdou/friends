package com.example.testfriends_jetpackcompose.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "on_boarding_pref")


class DataStoreRepository ( context: Context) {

    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
        val userInfo = stringPreferencesKey(name = "userInfo")
    }

    private val dataStore = context.dataStore
//
//    suspend fun saveOnBoardingState(completed: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[PreferencesKey.onBoardingKey] = completed
//        }
//    }
//
//    fun readOnBoardingState(): Flow<Boolean> {
//        return dataStore.data
//            .catch { exception ->
//                if (exception is IOException) {
//                    emit(emptyPreferences())
//                } else {
//                    throw exception
//                }
//            }
//            .map { preferences ->
//                val onBoardingState = preferences[PreferencesKey.onBoardingKey] ?: false
//                onBoardingState
//            }
//    }

    suspend fun saveUser(user: String){
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userInfo] = user
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