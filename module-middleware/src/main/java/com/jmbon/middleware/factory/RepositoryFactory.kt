package com.jmbon.middleware.factory

import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

internal class RepositoryFactory(private val factory: Producer.Factory) : Producer.Factory {

    override fun <T> getProducer(klass: Class<T>): Producer<T>? {
        if (Modifier.isAbstract(klass.modifiers)) {
            // 如果返回类型是抽象的，不支持
            return null
        }
        return klass.constructors.filterIsInstance<Constructor<T>>()
            .firstNotNullOfOrNull(::getFactory)
    }

    /** 获取构造器中所有的参数，如果支持创建所有的参数实例，则返回非空的[Producer]实例，否则返回null */
    private fun <T> getFactory(constructor: Constructor<T>): Producer<T>? {
        val list = mutableListOf<Producer<*>>()
        for (klass in constructor.parameterTypes) {
            val factory = factory.getProducer(klass) ?: return null
            list.add(factory)
        }
        return Producer {
            val parameters = list.map(Producer<*>::produce).toTypedArray()
            constructor.newInstance(*parameters)
        }
    }

}