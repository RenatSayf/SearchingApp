package com.fl.searchingapp.utils

import android.content.Context
import android.content.res.AssetManager
import java.io.IOException
import java.io.InputStream


object FileUtil
{
    private fun loadInputStreamFromAssetFile(context: Context, fileName: String?): InputStream?
    {
        val am: AssetManager = context.assets
        try
        {
            return am.open(fileName!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun loadContentFromFile(context: Context, path: String?): String? {
        val content: String?
        content = try
        {
            val input: InputStream? = loadInputStreamFromAssetFile(context, path)
            val size: Int? = input?.available()
            size?.let {
                val byteArray = ByteArray(it)
                input.read(byteArray)
                input.close()
                String(byteArray)
            }
        }
        catch (ex: IOException)
        {
            ex.printStackTrace()
            return null
        }.toString()
        return content
    }
}