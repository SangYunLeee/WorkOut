package com.example.workout.DataType
import java.time.LocalDate
import java.util.*

class RecordOfDay(
     var date : LocalDate,
     var listOfRecord : Vector<WO_Record>
){
     fun IsEmpty() : Boolean {
          var result : Boolean =  ( listOfRecord.size == 0 )
          return result
     }
}
