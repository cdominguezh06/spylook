package com.cogu.spylook.model.entity

import androidx.room.Entity

@Entity(primaryKeys = ["idContacto", "idAmigo"])
class ContactoAmistadCrossRef {
    @JvmField
    var idContacto: Int = 0
    @JvmField
    var idAmigo: Int = 0
}
