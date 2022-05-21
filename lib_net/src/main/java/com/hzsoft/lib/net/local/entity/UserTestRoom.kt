package com.hzsoft.lib.net.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/7
 */
@Entity
data class UserTestRoom(
    var image: String,
    var firstName: String,
    var lastName: String,
    var age: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
