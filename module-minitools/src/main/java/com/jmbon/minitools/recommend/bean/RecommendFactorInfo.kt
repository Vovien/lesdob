package com.jmbon.minitools.recommend.bean

/******************************************************************************
 * Description: 推荐维度信息
 *
 * Author: jhg
 *
 * Date: 2023/6/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
data class RecommendFactorInfo(
    /**
     * 类型: 1=我要试管，2=我要怀孕，3=我要保胎，4=我要育儿
     */
    var type: String = "",
    /**
     * 所在城市id
     */
    var cityId: Int = 0,
    /**
     * 试管原因值, 逗号分隔
     */
    var reasonValues: String = "",
    /**
     * 想了解的技术, 逗号分隔
     */
    var technicalValues: String = "",
    /**
     * 性别
     */
    var gender: Int = 0,
    /**
     * 年龄
     */
    var age: Int = 0,
    /**
     * 生育数量
     */
    var fertilityCount: Int = -1,
    /**
     * 助孕数量
     */
    var helpPregnantCount: Int = -1,
    /**
     * 妊娠数量
     */
    var pregnancyCount: Int = -1,
    /**
     * 妊娠方式
     */
    var pregnancyWay: Int = -1,
    /**
     * 婴儿年龄
     */
    var babyAge: Int = -1,
) {
    fun isFemale() = gender == 2
}