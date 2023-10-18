package com.example.vculp.shared.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStoreManager(private val context: Context) {
    companion object {
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "user_datastore")
        val ONBOARDING_FINISHED_STATUS_KEY = booleanPreferencesKey("ONBOARDING_KEY")
    }

    val onboarding_status: Flow<Boolean> = context.datastore.data
        .map { preferences ->
            // No type safety.
            preferences[ONBOARDING_FINISHED_STATUS_KEY] ?: false
        }

    suspend fun finishOnboarding(){
        context.datastore.edit {userDataStore ->
            userDataStore[ONBOARDING_FINISHED_STATUS_KEY] = true
        }
    }
}