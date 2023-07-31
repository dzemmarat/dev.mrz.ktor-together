package dev.mrz.data.model.local

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class UserLocal(
    val _id: Id<UserLocal> = newId(),
    val name: String,
    val surname: String,
    val avatar: String?,
    val role: String = "student",
    val phone: String,
    val passwordHashed: String,
)
