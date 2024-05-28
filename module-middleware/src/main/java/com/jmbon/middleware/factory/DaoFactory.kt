package com.jmbon.middleware.factory

import androidx.room.RoomDatabase
import java.lang.reflect.Method
import java.lang.reflect.Modifier

internal class DaoFactory(val database: RoomDatabase) : Producer.Factory {

    private val daoMethods = database.javaClass.methods.filter(::isAbstractReturnType)

    override fun <T> getProducer(klass: Class<T>): Producer<T>? {
        val method = daoMethods.find {
            it.returnType == klass
        } ?: return null
        return Producer {
            method.invoke(database).let(klass::cast)!!
        }
    }

    private fun isAbstractReturnType(method: Method): Boolean {
        return Modifier.isAbstract(method.returnType.modifiers)
    }

}