package dev.mrz.data.dataSources

import dev.mrz.data.model.local.CommunityNoteLocal
import org.bson.types.ObjectId
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.id.toId
import org.litote.kmongo.or

class CommunityNotesDataSourceImpl(
    database: CoroutineDatabase,
) : CommunityNotesDataSource {

    private val communityNoteCollection = database.getCollection<CommunityNoteLocal>()

    override suspend fun insertCommunityNote(communityNote: CommunityNoteLocal) =
        communityNoteCollection.insertOne(communityNote)

    override suspend fun getAllCommunityNotes(): List<CommunityNoteLocal> = communityNoteCollection.find().toList()

    override suspend fun getCommunityNoteById(id: String): CommunityNoteLocal? =
        communityNoteCollection.findOneById(ObjectId(id))

    override suspend fun getAllCommunityNotesByAuthorId(userId: String): List<CommunityNoteLocal> =
        communityNoteCollection.find(CommunityNoteLocal::authorId eq ObjectId(userId).toId()).toList()

    override suspend fun getCommunityNoteByQuery(query: String): List<CommunityNoteLocal> =
        communityNoteCollection.find(or(CommunityNoteLocal::title eq query, CommunityNoteLocal::textContent contains query))
            .toList()
}