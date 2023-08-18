package com.example.mnemonicwallet.utiles

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

object FileUtil {
    fun readJsonFile(file: File): String {
        var ret = ""

        try {
            val inputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            val stringBuilder = StringBuilder()
            var receiveString = bufferedReader.readLine()
            while (receiveString != null) {
                stringBuilder.append(receiveString)
                receiveString = bufferedReader.readLine()
            }

            inputStream!!.close()
            ret = stringBuilder.toString()
        } catch (e: FileNotFoundException) {
            return e.localizedMessage
        } catch (e: IOException) {
            return e.localizedMessage
        }
        return ret
    }
}
