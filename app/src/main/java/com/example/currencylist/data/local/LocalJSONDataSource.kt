package com.example.currencylist.data.local

import org.json.JSONArray

interface LocalJSONDataSource {
    suspend fun getJSONArrayFromLocalJSONFile(fileId: Int): JSONArray
}