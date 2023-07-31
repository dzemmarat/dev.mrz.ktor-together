package dev.mrz.di

import dev.mrz.data.dataSources.*
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        KMongo.createClient("mongodb://mongodb:27017")
            .coroutine
            .getDatabase("together_db")
    }

    single<CoursesDataSource> { CoursesDataSourceImpl(get()) }
    single<StatusOfCoursesDataSource> { StatusOfCoursesDataSourceImpl(get()) }
    single<UsersDataSource> { UsersDataSourceImpl(get()) }
    single<CommunityNotesDataSource> { CommunityNotesDataSourceImpl(get()) }
}