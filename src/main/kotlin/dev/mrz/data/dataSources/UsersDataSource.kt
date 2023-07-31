package dev.mrz.data.dataSources

import com.mongodb.client.result.InsertOneResult
import dev.mrz.data.model.local.UserLocal

interface UsersDataSource {
    suspend fun getAllUsers(): List<UserLocal>
    suspend fun isUserExist(phone: String): Boolean
    suspend fun getUserById(id: String): UserLocal?
    suspend fun getUserByCryptCredentials(phone: String, passwordMD: String): UserLocal?
    suspend fun insertUser(user: UserLocal): InsertOneResult
}