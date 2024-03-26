package com.example.currencylist.data.local

import android.content.Context
import org.json.JSONArray

class LocalJSONDataSourceImpl(val applicationContext: Context): LocalJSONDataSource {
    override suspend fun getJSONArrayFromLocalJSONFile(fileId: Int): JSONArray {
        return applicationContext.resources.openRawResource(fileId).bufferedReader().use {
            JSONArray(it.readText())
        }
    }

}