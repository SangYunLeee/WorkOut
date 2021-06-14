package com.example.workout.DataType

class WO_Record (
    // e.q
        // Type : 팔굽혀펴기
        // sum  : 100 개
        // cnt  : 5 회
    var type : String,
    var sum : Int,
    var cnt : Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WO_Record) return false

        if (type != other.type) return false
        if (sum != other.sum) return false
        if (cnt != other.cnt) return false

        return true
    }
}