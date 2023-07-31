package dev.mrz.data.dataSources

import com.mongodb.client.result.InsertOneResult
import dev.mrz.data.model.local.UserLocal
import dev.mrz.isNotNull
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UsersDataSourceImpl(
    database: CoroutineDatabase,
) : UsersDataSource {

    private val usersCollection = database.getCollection<UserLocal>()

    override suspend fun getAllUsers(): List<UserLocal> = usersCollection.find().toList()

    override suspend fun isUserExist(phone: String): Boolean =
        usersCollection.findOne(UserLocal::phone eq phone).isNotNull()

    override suspend fun getUserById(id: String): UserLocal? = usersCollection.findOneById(ObjectId(id))

    override suspend fun getUserByCryptCredentials(phone: String, passwordMD: String): UserLocal? =
        usersCollection.findOne(UserLocal::phone eq phone).takeIf { it?.passwordHashed == passwordMD }

    override suspend fun insertUser(user: UserLocal): InsertOneResult =
        usersCollection.insertOne(user)
}