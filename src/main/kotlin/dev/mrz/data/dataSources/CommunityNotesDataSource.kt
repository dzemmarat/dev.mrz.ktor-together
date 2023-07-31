package dev.mrz.data.dataSources

import com.mongodb.client.result.InsertOneResult
import dev.mrz.data.model.local.CommunityNoteLocal

interface CommunityNotesDataSource {
    suspend fun insertCommunityNote(communityNote: CommunityNoteLocal): InsertOneResult
    suspend fun getAllCommunityNotes(): List<CommunityNoteLocal>
    suspend fun getCommunityNoteById(id: String): CommunityNoteLocal?
    suspend fun getAllCommunityNotesByAuthorId(userId: String): List<CommunityNoteLocal>
    suspend fun getCommunityNoteByQuery(query: String): List<CommunityNoteLocal>
}