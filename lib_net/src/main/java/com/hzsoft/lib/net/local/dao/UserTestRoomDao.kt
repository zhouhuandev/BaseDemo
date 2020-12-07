package com.hzsoft.lib.net.local.dao

import androidx.room.*
import com.hzsoft.lib.net.local.entity.UserTestRoom

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/7
 */
@Dao
interface UserTestRoomDao {

    @Insert
    fun insertUserTestRoom(userTestRoom: UserTestRoom): Long

    @Update
    fun updateUserTestRoom(newUserTestRoom: UserTestRoom)

    @Query("SELECT * FROM USERTESTROOM")
    fun loadAllUserTestRooms(): List<UserTestRoom>

    @Query("SELECT * FROM USERTESTROOM WHERE AGE > :age")
    fun loadUserTestRoomOlderThan(age: Int): List<UserTestRoom>

    @Delete
    fun deleteUserTestRoom(deleteUserTestRoom: UserTestRoom)

    @Query("DELETE FROM USERTESTROOM WHERE lastName = :lastName")
    fun deleteUserTestRoomByLastName(lastName: String): Int
}