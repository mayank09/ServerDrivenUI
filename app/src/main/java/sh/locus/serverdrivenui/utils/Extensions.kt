package sh.locus.serverdrivenui.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sh.locus.serverdrivenui.model.Response
import java.io.IOException

fun Context.getResponseFromAsset(fileName: String): Resource<Response> {

    val jsonString: String

    try {
        jsonString = assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return Resource.Error(message = ioException.message!!)
    }

    Log.d("response", jsonString)

    val responseType = object : TypeToken<Response>() {}.type
    return Resource.Success(data = Gson().fromJson(jsonString, responseType))
}