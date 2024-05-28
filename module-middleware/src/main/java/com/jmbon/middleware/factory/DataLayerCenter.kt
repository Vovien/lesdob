package com.jmbon.middleware.factory

import androidx.room.RoomDatabase
import java.util.LinkedList

/** 使用此方法快捷调用[DataLayerCenter.getInstance]方法 */
inline fun <reified T> inject(): Lazy<T> = lazy(DataLayerCenter::getInstance)

/**
 * 数据层中心
 * 帮助用户创建RoomDatabase的Dao实例的辅助工具。
 * 也可以创建Repository的实例，前提是它的构造器中的参数只有Dao类型。
 * 可通过以下代码快速创建：
 * ```
 * //方法1：在构造器中指定要使用的Dao
 * class UserRepository(val userDao: UserDao)
 * //创建UserRepository的实例
 * val userRepository: UserRepository by inject()
 * //方法2：注入成员
 * class UserRepository {
 *      //获取UserDao的实例
 *      val userDao: UserDao by inject()
 * }
 *
 * ```
 */
object DataLayerCenter : Producer.Factory {

    private val factoryProducers = LinkedList<Producer.Factory>()

    init {
        addFactory(RepositoryFactory(this))
    }

    /** 设置DAO生产者 */
    fun addDaoFactory(vararg databases: RoomDatabase) {
        for (database in databases) {
            addFactory(DaoFactory(database))
        }
    }

    /** 添加生产工厂，可支持更多类型 */
    private fun addFactory(producer: Producer.Factory) {
        factoryProducers.addFirst(producer)
    }

    override fun <T> getProducer(klass: Class<T>): Producer<T> {
        for (factoryProducer in factoryProducers) {
            val factory = factoryProducer.getProducer(klass)
            if (factory != null) {
                return factory
            }
        }
        throw NullPointerException("无法创建${klass}的实例。")
    }

    fun <T> getInstance(klass: Class<T>) = getProducer(klass).produce()

    inline fun <reified T> getInstance() = getInstance(T::class.java)

}