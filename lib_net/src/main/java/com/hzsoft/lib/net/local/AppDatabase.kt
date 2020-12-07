package com.hzsoft.lib.net.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hzsoft.lib.net.local.dao.UserTestRoomDao
import com.hzsoft.lib.net.local.entity.UserTestRoom

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/7
 */
@Database(version = 1, entities = [UserTestRoom::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun userTestRoomDao(): UserTestRoomDao

    // abstract fun book(): BookDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("create table Book (id integer primary key autoincrement not null,name text not null,pages integer not null)")
            }
        }

        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "app_database"
            )
                // 每次数据库升级增加表都需要写在这里
                .addMigrations(MIGRATION_1_2)
                // 只要升级，销毁当前数据库
                .fallbackToDestructiveMigration()
                // 默认不能在主线程里面进行查询，需要打开一下方法
                // .allowMainThreadQueries()
                .build().apply { instance = this }
        }
    }
}