package com.yjotdev.clasificarpeces.utils.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.clasificarpeces.domain.repository.StringRepository

@Singleton
class FakeStringRepositoryImpl @Inject constructor(): StringRepository {

    override fun getString(resId: Int): String {
        return "Id del recurso: $resId"
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return "Id del recurso: $resId y argumentos: $args"
    }
}