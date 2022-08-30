package com.yallina.myapplication.data.local_file.task

import com.yallina.myapplication.data.local_file.base.AssetReader
import com.yallina.myapplication.data.local_file.base.JsonDeserializer
import com.yallina.myapplication.data.local_file.task.model.SerializedTask
import java.io.InputStream

/**
 * [AssetReader] implementation with [SerializedTask] type.
 * @param jsonDeserializer a [JsonDeserializer] object with default value of [JsonDeserializerImpl]
 */
class TaskAssetReaderImpl(
    private val jsonDeserializer: JsonDeserializer<SerializedTask> = JsonDeserializerImpl
) : AssetReader<SerializedTask> {

    @Throws(Exception::class)
    override fun readOne(input: InputStream): SerializedTask {
        val inputStr = input.bufferedReader().use { it.readText() }
        return jsonDeserializer.deserializeOne(inputStr)
    }

    @Throws(Exception::class)
    override fun readList(input: InputStream): List<SerializedTask> {
        val inputStr = input.bufferedReader().use { it.readText() }
        return jsonDeserializer.deserializeList(inputStr)
    }
}
