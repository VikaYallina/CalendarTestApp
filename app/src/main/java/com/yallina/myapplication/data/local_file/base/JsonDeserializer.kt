package com.yallina.myapplication.data.local_file.base

/**
 * Interface that is used to deserialize an input json string into either
 * serialized object or list of serialized objects of generic type T
 */
interface JsonDeserializer<T> {

    /**
     * Deserializes a json string into a single serialized object
     * @param json input json String
     * @return a single serialized object of generic type T
     */
    fun deserializeOne(json: String): T

    /**
     * Deserializes a json string into a list serialized objects
     * @param json input json String
     * @return a list of serialized objects of generic type T
     */
    fun deserializeList(json: String): List<T>
}