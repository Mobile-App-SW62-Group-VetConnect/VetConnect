package com.luciano.vetconnect.shared.data.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(formatter.format(value))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun read(input: JsonReader): LocalDateTime? {
        val dateString = input.nextString()
        return try {
            LocalDateTime.parse(dateString, formatter)
        } catch (e: Exception) {
            null
        }
    }
}