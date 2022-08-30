package com.yallina.myapplication.data.local_file.task

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.yallina.myapplication.data.local_file.base.JsonDeserializer
import com.yallina.myapplication.data.local_file.task.model.SerializedTask
import com.yallina.myapplication.data.local_file.util.LocalDateTimeDeserializer
import org.threeten.bp.LocalDateTime

/**
 * [JsonDeserializer] implementation with [SerializedTask] type
 * Uses [Gson] library as a deserializer
 */
object JsonDeserializerImpl: JsonDeserializer<SerializedTask> {

    private var gson: Gson

    init {
        val gsonBuilder = GsonBuilder()

        gsonBuilder.registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())

        gson = gsonBuilder.create()
    }

    @Throws(JsonSyntaxException::class)
    override fun deserializeOne(json: String): SerializedTask {
        return gson.fromJson(json, SerializedTask::class.java)
    }

    @Throws(JsonSyntaxException::class)
    override fun deserializeList(json: String): List<SerializedTask> {
        val typeToken = object : TypeToken<List<SerializedTask>>() {}.type
        return gson.fromJson(json, typeToken)
    }
}