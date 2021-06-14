package com.example.workout.DataType
import java.time.LocalDate
import java.util.*

class RecordOfDay(
     var date : LocalDate,
     var listOfRecord : Vector<WO_Record>
){
     override fun equals(other: Any?): Boolean {
          if (this === other) return true
          if (other !is RecordOfDay) return false

          if (date != other.date) return false
          if (listOfRecord != other.listOfRecord) return false

          return true
     }

}
