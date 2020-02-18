package ru.skillbranch.gameofthrones.data.remote.res

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.utils.lastNumbersOrEmpty

data class HouseRes(
    val url: String,
    val name: String,
    val region: String,
    val coatOfArms: String,
    val words: String,
    val titles: List<String> = listOf(),
    val seats: List<String> = listOf(),
    val currentLord: String,
    val heir: String,
    val overlord: String,
    val founded: String,
    val founder: String,
    val diedOut: String,
    val ancestralWeapons: List<String> = listOf(),
    val cadetBranches: List<Any> = listOf(),
    val swornMembers: List<String> = listOf()) {

    class Deserializer : ResponseDeserializable<Array<HouseRes>> {
        override fun deserialize(content: String): Array<HouseRes>? =
            Gson().fromJson(content, Array<HouseRes>::class.java)
    }

    class DeserializerSimple: ResponseDeserializable<HouseRes> {
        override fun deserialize(content: String): HouseRes? =
            Gson().fromJson(content, HouseRes::class.java)
    }

    fun toHouse(): House {
        val h = House( id=url.lastNumbersOrEmpty(),
            name=this.name,
            region=this.region,
            coatOfArms = this.coatOfArms,
            words = this.words,
            titles = this.titles,
            seats = this.seats,
            currentLord = this.currentLord.lastNumbersOrEmpty(),
            heir = this.heir.lastNumbersOrEmpty(),
            overlord = this.overlord.lastNumbersOrEmpty(),
            founded = this.founded,
            founder = this.founder.lastNumbersOrEmpty(),
            diedOut = this.diedOut,
            ancestralWeapons = this.ancestralWeapons)
        return h
    }

}