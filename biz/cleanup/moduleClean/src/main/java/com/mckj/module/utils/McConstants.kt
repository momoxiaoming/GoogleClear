package com.mckj.module.utils

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/15 10:50
 * @desc
 */
object McConstants {
    /**
     * 排序
     */
    object Sort {
        const val SORT_NEWEST = 1 shl 0
        const val SORT_LAST = 1 shl 1
        const val SORT_MAX = 1 shl 2
        const val SORT_MIN = 1 shl 3
    }

    object Filter {
        const val FILTER_WORD = 1 shl 4
        const val FILTER_EXCEL = 1 shl 5
        const val FILTER_PPT = 1 shl 6
        const val FILTER_PDF = 1 shl 7
        const val FILTER_TXT = 1 shl 8
        const val FILTER_OTHER = 1 shl 9
        const val FILTER_ALL = 0
    }
}