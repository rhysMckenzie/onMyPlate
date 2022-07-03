package com.personal.onmyplate.models

import java.io.Serializable
import java.time.OffsetDateTime

class Task: Serializable {
    var name: String = ""
    var description: String = ""
    var isDueAsap: Boolean = false
    var dueDate: OffsetDateTime = OffsetDateTime.now()
}