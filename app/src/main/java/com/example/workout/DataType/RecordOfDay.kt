package com.example.workout.DataType
import android.net.Uri
import java.time.LocalDate
import java.util.*

class RecordOfDay(
     var date : String,
     var listOfRecord : Vector<WO_Record>
){
     var picturePath: String? = null
     var weight: String = ""

     override fun equals(other: Any?): Boolean {
          if (this === other) return true
          if (other !is RecordOfDay) return false

          if (date != other.date) return false
          if (listOfRecord != other.listOfRecord) return false

          return true
     }

}
