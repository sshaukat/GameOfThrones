package ru.skillbranch.gameofthrones.repositories

import androidx.annotation.VisibleForTesting
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import kotlinx.coroutines.*
import ru.skillbranch.gameofthrones.AppConfig.BASE_URL
import ru.skillbranch.gameofthrones.data.local.entities.*
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.utils.MyApp
import ru.skillbranch.gameofthrones.utils.getHouseId
import ru.skillbranch.gameofthrones.utils.lastNumbersOrEmpty
import kotlin.coroutines.CoroutineContext

object RootRepository {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private lateinit var database: GotDatabase
    private val houseDao: HouseDao
    private val characterDao: CharacterDao


    init {
        database = GotDatabase.getDatabase(MyApp.applicationContext)
        houseDao = database.houseDao
        characterDao = database.characterDao
    }

    /**
     * Получение данных о всех домах
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getAllHouses(result : (houses : List<HouseRes>) -> Unit) {
        scope.launch(Dispatchers.Default) {
            val housesRes = arrayListOf<HouseRes>()
            var currentPage = 1
            var existsResult = true;
            while(existsResult) {
                existsResult = false
                Fuel.get(BASE_URL + "houses",listOf("page" to currentPage.toString(), "pageSize" to "50"))
                    .awaitObjectResult(HouseRes.Deserializer()).get().forEach {
                        housesRes.add(it)
                        existsResult = true
                    }
                currentPage++
            }
            result(housesRes)
        }}


    /**
     * Получение данных о требуемых домах по их полным именам (например фильтрация всех домов)
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouses(vararg houseNames: String, result : (houses : List<HouseRes>) -> Unit) {
        scope.launch(Dispatchers.Default) {
            val housesRes = arrayListOf<HouseRes>()
                houseNames.forEach {
                    Fuel.get(BASE_URL + "houses", listOf("name" to it))
                        .awaitObjectResult(HouseRes.Deserializer()).get().forEach { housesRes.add(it)}
                }
            result(housesRes)
        }}

    /**
     * Получение данных о требуемых домах по их полным именам и персонажах в каждом из домов
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о доме и персонажей в нем (Дом - Список Персонажей в нем)
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouseWithCharacters(vararg houseNames: String, result : (houses : List<Pair<HouseRes, List<CharacterRes>>>) -> Unit) {
        val housePersonsPair  = arrayListOf<Pair<HouseRes, List<CharacterRes>>>()
        scope.launch(Dispatchers.Default) {
            houseNames.forEach {
                Fuel.get(BASE_URL + "houses", listOf("name" to it))
                    .awaitObjectResult(HouseRes.Deserializer()).get().forEach {
                        housePersonsPair.add(
                            Pair(it, it.swornMembers.map {
                                Fuel.get(it).awaitObjectResult(CharacterRes.Deserializer()).get()
                        }))}}
            result(housePersonsPair)
        }}

    /**
     * Запись данных о домах в DB
     * @param houses - Список персонажей (модель HouseRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertHouses(houses : List<HouseRes>, complete: () -> Unit) {
        scope.launch(Dispatchers.Default) {houses.forEach {
            houseDao.insert(it.toHouse())}
            complete()
        }}

    private suspend fun getCharacterRes(characterURL: String):CharacterRes {
        return Fuel.get(characterURL).awaitObjectResult(CharacterRes.Deserializer()).get()
    }

    private suspend fun getHouseRes(houseURL: String):HouseRes {
        return Fuel.get(houseURL).awaitObjectResult(HouseRes.DeserializerSimple()).get()
    }

    private suspend fun getRelativeInfo(urlRelative: String): RelativeCharacter? {
        val relList = characterDao.findCharacterFullById(urlRelative.lastNumbersOrEmpty())
        return if (relList.isEmpty()) {
            val relRes = getCharacterRes(urlRelative)
            val relHouse = getHouseRes(relRes.allegiances[0])
            RelativeCharacter(urlRelative.lastNumbersOrEmpty(), relRes.name, relHouse.name.getHouseId() ) // get from internet
        } else
            RelativeCharacter(urlRelative.lastNumbersOrEmpty(),relList[0].name, relList[0].house) // get from Db
    }

     /**
     * Запись данных о пересонажах в DB
     * @param characters - Список персонажей (модель CharterRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertCharacters(characters : List<CharacterRes>, complete: () -> Unit) {
        scope.launch(Dispatchers.Default) {
            characters.forEach {
                val motherRec =  if (it.mother.isNotBlank()) { // get Mother info (house+name)
                    val relMother = getRelativeInfo(it.mother)
                    "${relMother?.id};${relMother?.name};${relMother?.house}"
                } else ";;";

                val fatherRec =  if (it.father.isNotBlank()) {// get father info (house+name)
                    val relFather = getRelativeInfo(it.father)
                    "${relFather?.id};${relFather?.name};${relFather?.house}"
                } else ";;";

                val charGen = it.toCharacter()
                charGen.mother = motherRec
                charGen.father = fatherRec
                characterDao.insert(charGen)
            }
            complete()
        }}

    /**
     * При вызове данного метода необходимо выполнить удаление всех записей в db
     * @param complete - колбек о завершении очистки db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun dropDb(complete: () -> Unit) {
        scope.launch(Dispatchers.Default) {
            characterDao.clear()
            houseDao.clear()
            complete()
        }}

    /**
     * Поиск всех персонажей по имени дома, должен вернуть список краткой информации о персонажах
     * дома - смотри модель CharacterItem
     * @param name - краткое имя дома (его первычный ключ)
     * @param result - колбек содержащий в себе список краткой информации о персонажах дома
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharactersByHouseName(name : String, result: (characters : List<CharacterItem>) -> Unit) {
        var characters = listOf<CharacterItem>()
        scope.launch(Dispatchers.Default) {
            characters = characterDao.findCharactersByHouseName(name)
            result(characters)
        }}

    /**
     * Поиск персонажа по его идентификатору, должен вернуть полную информацию о персонаже
     * и его родственных отношения - смотри модель CharacterFull
     * @param id - идентификатор персонажа
     * @param result - колбек содержащий в себе полную информацию о персонаже
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharacterFullById(id : String, result: (character : CharacterFull) -> Unit) {
        var characters : List<CharacterFull> = listOf()
        scope.launch(Dispatchers.Default)  {
            characters = characterDao.findCharacterFullById(id)
            result(characters[0])
        }}

    /**
     * Метод возвращет true если в базе нет ни одной записи, иначе false
     * @param result - колбек о завершении очистки db
     */
    fun isNeedUpdate(result: (isNeed : Boolean) -> Unit){
        scope.launch(Dispatchers.Default)  {
            result(houseDao.getCount()==0)
        }}
}