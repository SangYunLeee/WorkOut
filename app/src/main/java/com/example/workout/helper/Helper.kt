package com.example.workout.helper


import android.content.Context
import android.renderscript.Element
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.RoundingMode
import java.text.DecimalFormat
import com.example.workout.DataType.RecordOfDay
import com.example.workout.DataType.WO_Record
import java.lang.reflect.Type
import java.time.LocalDate
import java.util.*


class Helper {
    companion object {
        fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }

        fun pushToSP(value : Any?, key : String, storageName : String, context : Context) {
            val prefs = context.getSharedPreferences(storageName, AppCompatActivity.MODE_PRIVATE)
            val prefsEditor  = prefs.edit()
            val gson = Gson()
            val jsonSaving = gson.toJson(value)
            prefsEditor.putString(key, jsonSaving)
            prefsEditor.apply()
        }

        inline fun <reified T> getFromSP(key: String, storageName: String, context: Context) : T? {
            val type = object : TypeToken<T>() {}.type
            var record: T?
            val prefs = context.getSharedPreferences(storageName, AppCompatActivity.MODE_PRIVATE)
            val json = prefs.getString( key, "")
            record = try {
                Gson().fromJson(json, type )
            } catch (e : Exception){
                null
            }
            return record
        }
    }
}