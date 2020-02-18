package ru.skillbranch.gameofthrones.data.remote.res

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterGeneral
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.RelativeCharacter
import ru.skillbranch.gameofthrones.utils.lastNumbersOrEmpty

data class CharacterRes(
    val url: String,
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String> = listOf(),
    val aliases: List<String> = listOf(),
    val father: String,
    val mother: String,
    val spouse: String,
    val allegiances: List<String> = listOf(),
    val books: List<String> = listOf(),
    val povBooks: List<Any> = listOf(),
    val tvSeries: List<String> = listOf(),
    val playedBy: List<String> = listOf()){

    class Deserializer : ResponseDeserializable<CharacterRes> {
        override fun deserialize(content: String): CharacterRes? =
            Gson().fromJson(content, CharacterRes::class.java)
    }
    var houseId: String? = null

    fun toCharacter(): CharacterGeneral {
        val ali: String = if (allegiances.size==0) houseId!! else allegiances[0].lastNumbersOrEmpty()
        return CharacterGeneral(
            url.lastNumbersOrEmpty(),
            name,
            gender,
            culture,
            born,
            died,
            titles,
            aliases,
            father.lastNumbersOrEmpty()+";stub1;stub2",
            mother.lastNumbersOrEmpty()+";stub1;stub2",
            spouse,
            houseId,
            ali,
            words=""
        )
    }
}