package ru.skillbranch.gameofthrones

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.repositories.RootRepository.getNeedHouses

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //test
        RootRepository.getNeedHouseWithCharacters("House Stark of Winterfell", "House Lannister of Casterly Rock"){
            it.forEach{
                println("$it.first.name")
                it.second.forEach{
                    println("$it.name")
                }
            }
        }
    }
}
