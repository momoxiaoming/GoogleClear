package com.mckj.api.impl.parser

import com.mckj.api.db.entity.JunkDbEntity
import com.mckj.api.entity.AppJunk
import io.reactivex.rxjava3.functions.Consumer
/**
 * @author leix
 * @version 1
 * @createTime 2021/12/22 9:20
 * @desc
 */
interface IParser {
    fun parseJunk(
        map: Map<String, MutableList<JunkDbEntity>>,
        consumer: Consumer<AppJunk>,
        type:Int
    )
    fun stop()
}