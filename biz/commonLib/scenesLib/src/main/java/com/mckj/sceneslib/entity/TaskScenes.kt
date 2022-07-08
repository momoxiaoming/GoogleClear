package com.mckj.sceneslib.entity

/**
 * 任务
 *
 * @author mmxm
 * @date 2021/5/11 11:33
 */
object TaskScenes {

    //清理
    object Clean {
        /**
         * 无任务,各场景可自行实现相应逻辑
         */
        const val UNKNOWN = "task_clean_unknown"
        /**
         * 常规清理任务
         */
        const val COMMON = "task_clean_common"

        /**
         * 清理内存
         */
        const val RAM = "task_clean_ram"

        /**
         * 安装包清理
         */
        const val APK = "task_clean_apk"

        /**
         * 卸载残留
         */
        const val RESIDUE = "task_clean_residue"
    }

    /**
     * 扫描
     */
    object Scan {
        /**
         * 常规垃圾扫描
         */
        const val JUNK = "task_scan_junk"

        /**
         * 常规垃圾扫描
         */
        const val RAM = "task_scan_ram"
    }
}