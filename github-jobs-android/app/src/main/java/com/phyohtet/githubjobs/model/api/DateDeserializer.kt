package com.phyohtet.githubjobs.model.api

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : StdDeserializer<Date>(Date::class.java) {

    private val dateFormat = SimpleDateFormat("c", Locale.ENGLISH)

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Date? {

        try {
            p?.text?.also {
                return dateFormat.parse(it)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }
}