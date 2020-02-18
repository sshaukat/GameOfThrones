package ru.skillbranch.gameofthrones.data.local.entities

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope

@Database(entities = [House::class, CharacterGeneral::class], version=3)
@TypeConverters(ListStrConverters::class, RelativeCharacterStrConverters::class)
abstract class GotDatabase: RoomDatabase() {

    // all user DAOs
    abstract val houseDao: HouseDao
    abstract val characterDao: CharacterDao
    companion object {
        @Volatile
        private var INSTANCE: GotDatabase? = null // only one

        fun getDatabase(context: Context): GotDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) { // Создаем новый, если не создан
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GotDatabase::class.java,
                        "GoT database"
                    )
                        .fallbackToDestructiveMigration()
                        //.addCallback(VocDatabaseCallback(scope))
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

class ListStrConverters {
    @TypeConverter
    fun fromList(value: List<String>?): String? {
        return value?.joinToString(";")
    }

    @TypeConverter
    fun toList(value: String?): List<String>? {
        return value?.split(";")
    }
}

class RelativeCharacterStrConverters {
    @TypeConverter
    fun fromRelativeCharter(value: RelativeCharacter?): String? {
        return if (value != null) {
            "${value.id};${value.name};${value.house}"
        } else null
    }

    @TypeConverter
    fun toRelativeCharter(value: String?): RelativeCharacter? {
        return if (value != null) {
            val fields = value.split(";")
            RelativeCharacter(fields[0], fields[1], fields[2])
        } else null
    }
}


