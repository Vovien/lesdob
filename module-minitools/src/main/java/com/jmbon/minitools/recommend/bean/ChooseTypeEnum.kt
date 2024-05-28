package com.jmbon.minitools.recommend.bean

/******************************************************************************
 * Description: 选择类型
 *
 * Author: jhg
 *
 * Date: 2023/5/31
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
enum class ChooseTypeEnum(val value: Int) {

    /**
     * 性别
     */
    CHOOSE_TYPE_GENDER(1),

    /**
     * 年龄
     */
    CHOOSE_TYPE_AGE(2),

    /**
     * 生育史
     */
    CHOOSE_TYPE_FERTILITY_HISTORY(3),

    /**
     * 助孕史
     */
    CHOOSE_TYPE_HELP_PREGNANT_HISTORY(4),

    /**
     * 妊娠史
     */
    CHOOSE_TYPE_PREGNANT_HISTORY(5),

    /**
     * 妊娠方式
     */
    CHOOSE_TYPE_PREGNANT_TYPE(6),

    /**
     * 胎龄
     */
    CHOOSE_TYPE_BABY_AGE(7),

}