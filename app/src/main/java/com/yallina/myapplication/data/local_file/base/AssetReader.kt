package com.yallina.myapplication.data.local_file.base

import java.io.InputStream

/**
 * Interface for reading asset files as [InputStream] and returning single object or list of objects
 * of generic type T
 */
interface AssetReader<T> {

    /**
     * Process [InputStream] into serialized object of generic type
     * @param input an [InputStream] of an asset file
     * @return serialized object of generic type T
     */
    fun readOne(input: InputStream): T

    /**
     * Process [InputStream] into a list of serialized objects of generic type
     * @param input an [InputStream] of an asset file
     * @return serialized list of objects of generic type T
     */
    fun readList(input: InputStream): List<T>
}