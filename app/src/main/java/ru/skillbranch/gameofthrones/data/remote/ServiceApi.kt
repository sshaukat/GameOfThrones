package ru.skillbranch.gameofthrones.data.remote

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryName
import ru.skillbranch.gameofthrones.data.remote.res.CharterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

interface ServiceApi {
    @GET("bjcjq0ra3hhoexr/ifd_menu_ver_2.json?dl=1")
    fun getHouse(@QueryName params: MutableMap<String, String>): Single<HouseRes>

    @GET("bjcjq0ra3hhoexr/ifd_menu_ver_2.json?dl=1")
    fun getCharter(@QueryName params: MutableMap<String, String>): Single<CharterRes>
}