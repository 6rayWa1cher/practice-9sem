package com.a6raywa1cher.graph

import org.koin.dsl.module

val graphModule = module {
    single<GraphService> { GraphServiceImpl() }
}