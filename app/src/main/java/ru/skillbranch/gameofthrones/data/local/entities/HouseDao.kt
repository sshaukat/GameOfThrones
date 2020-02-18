package ru.skillbranch.gameofthrones.data.local.entities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HouseDao {
    @Query("select * from houses order by name")
    fun getAll(): LiveData<List<House>> // Список словарей не меняется по ходу программы , иначе MutableLiveData

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(house: House)

    @Query("select * from houses where id=:idHouse")
    suspend fun getById(idHouse: String): House?

    @Query("select count(*) from houses")
    suspend fun getCount():Int

    @Query("delete from houses")
    suspend fun clear()
}