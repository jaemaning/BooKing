package com.ssafy.data.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime> {
    override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }
}

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
        if (json != null && json.isJsonArray) {
            val jsonArray = json.asJsonArray
            return LocalDateTime.of(
                jsonArray[0].asInt, // Year
                jsonArray[1].asInt, // Month
                jsonArray[2].asInt, // Day
                jsonArray[3].asInt, // Hour
                jsonArray[4].asInt, // Minute
                jsonArray[5].asInt, // Second
                jsonArray[6].asInt  // Nano
            )
        }
        throw JsonParseException("Unexpected JSON type: " + json.toString())
    }
//    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
//        return LocalDateTime.parse(json?.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//    }
}
