package ru.skillbranch.gameofthrones.di

import org.koin.dsl.module
import retrofit2.Retrofit
import ru.skillbranch.gameofthrones.data.remote.ServiceApi

val apiModule = module {
    single(createdAtStart = false) { get<Retrofit>().create(ServiceApi::class.java) }
}