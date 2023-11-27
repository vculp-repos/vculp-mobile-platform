package com.example.vculp.shared.data.converter

import androidx.room.TypeConverter
import com.example.vculp.shared.data.models.Link
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LinkTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun linkListToJson(links: List<Link>?): String? {
        return links?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun jsonToLinkList(json: String?): List<Link>? {
        return json?.let {
            val type = object : TypeToken<List<Link>>() {}.type
            gson.fromJson<List<Link>>(it, type)
        }
    }
}
