package com.otuolabs.unitconverter.miscellaneous

interface JsonConvertible {
    fun toJson(): String

    companion object {
        fun List<JsonConvertible>.toJson(): String {
            if (isEmpty()) return "[]"
            return buildString {
                append("[")
                this@toJson.forEach {
                    append(it.toJson())
                    append(",")
                }
                delete(length - 1, length)// deletes the last comma
                append("]")
            }
        }
    }
}