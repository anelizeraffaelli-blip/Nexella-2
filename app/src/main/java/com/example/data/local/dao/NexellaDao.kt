package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.BusinessEntity
import com.example.data.local.entity.ConnectionEntity
import com.example.data.local.entity.EllaMessageEntity
import com.example.data.local.entity.MeetingEntity
import com.example.data.local.entity.MeetingParticipantEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.ProfileEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NexellaDao {

    // --- USERS ---
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE status = 'Aprovado' ORDER BY createdAt DESC")
    fun getApprovedUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Long)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM users WHERE isCorretora = 1")
    fun getCorretoraCount(): Flow<Int>

    // --- PROFILES ---
    @Query("SELECT * FROM profiles WHERE userId = :userId LIMIT 1")
    fun getProfileByUserId(userId: Long): Flow<ProfileEntity?>

    @Query("SELECT * FROM profiles ORDER BY createdAt DESC")
    fun getAllProfiles(): Flow<List<ProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity): Long

    @Update
    suspend fun updateProfile(profile: ProfileEntity)

    @Query("DELETE FROM profiles WHERE id = :id")
    suspend fun deleteProfile(id: Long)

    // --- BUSINESSES ---
    @Query("SELECT * FROM businesses WHERE userId = :userId LIMIT 1")
    fun getBusinessByUserId(userId: Long): Flow<BusinessEntity?>

    @Query("SELECT * FROM businesses ORDER BY createdAt DESC")
    fun getAllBusinesses(): Flow<List<BusinessEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusiness(business: BusinessEntity): Long

    @Update
    suspend fun updateBusiness(business: BusinessEntity)

    @Query("DELETE FROM businesses WHERE id = :id")
    suspend fun deleteBusiness(id: Long)

    // --- OPPORTUNITIES ---
    @Query("SELECT * FROM opportunities WHERE status = 'Aprovada' ORDER BY createdAt DESC")
    fun getApprovedOpportunities(): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities WHERE city = :city AND (:neighborhood = 'Todos os Bairros' OR neighborhood = :neighborhood) AND status = 'Aprovada' ORDER BY createdAt DESC")
    fun getOpportunitiesByCityAndNeighborhood(city: String, neighborhood: String): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities ORDER BY createdAt DESC")
    fun getAllOpportunities(): Flow<List<OpportunityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunity(opportunity: OpportunityEntity): Long

    @Update
    suspend fun updateOpportunity(opportunity: OpportunityEntity)

    @Query("DELETE FROM opportunities WHERE id = :id")
    suspend fun deleteOpportunity(id: Long)

    @Query("SELECT COUNT(*) FROM opportunities")
    fun getOpportunityCount(): Flow<Int>

    // --- CONNECTIONS ---
    @Query("SELECT * FROM connections ORDER BY createdAt DESC")
    fun getAllConnections(): Flow<List<ConnectionEntity>>

    @Query("SELECT * FROM connections WHERE requesterId = :userId OR recipientId = :userId ORDER BY createdAt DESC")
    fun getUserConnections(userId: Long): Flow<List<ConnectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConnection(connection: ConnectionEntity): Long

    @Update
    suspend fun updateConnection(connection: ConnectionEntity)

    @Query("SELECT COUNT(*) FROM connections")
    fun getConnectionCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM connections WHERE generatedOpportunity = 1")
    fun SuccessfulDealsCount(): Flow<Int>

    // --- MEETINGS ---
    @Query("SELECT * FROM meetings ORDER BY id ASC")
    fun getAllMeetings(): Flow<List<MeetingEntity>>

    @Query("SELECT * FROM meetings WHERE id = :id")
    suspend fun getMeetingById(id: Long): MeetingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeeting(meeting: MeetingEntity): Long

    @Update
    suspend fun updateMeeting(meeting: MeetingEntity)

    @Query("SELECT COUNT(*) FROM meetings")
    fun getMeetingCount(): Flow<Int>

    // --- MEETING PARTICIPANTS ---
    @Query("SELECT * FROM meeting_participants WHERE meetingId = :meetingId")
    fun getParticipantsForMeeting(meetingId: Long): Flow<List<MeetingParticipantEntity>>

    @Query("SELECT COUNT(*) FROM meeting_participants WHERE meetingId = :meetingId AND userId = :userId")
    suspend fun isUserRegisteredForMeeting(meetingId: Long, userId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeetingParticipant(participant: MeetingParticipantEntity)

    // --- ELLA MESSAGES ---
    @Query("SELECT * FROM ella_messages ORDER BY timestamp ASC")
    fun getAllEllaMessages(): Flow<List<EllaMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEllaMessage(message: EllaMessageEntity): Long

    @Query("DELETE FROM ella_messages")
    suspend fun clearEllaMessages()
}
