package com.mckj.sceneslib.manager.task

import com.mckj.sceneslib.entity.TaskScenes
import kotlinx.coroutines.delay

/**
 * 任务管理器
 *
 * @author mmxm
 * @date 2021/5/11 10:37
 */
class TaskManager {

    companion object {
        const val TAG = "TaskManager"

        private val INSTANCE by lazy {
            TaskManager()
        }

        fun getInstance() = INSTANCE
    }

    private val taskPool: ITask = TaskPool()


    suspend fun runTask(taskTag: String): Any {
        return when (taskTag) {
            TaskScenes.Scan.RAM -> {
                taskPool.scanRunningApp()
            }
            TaskScenes.Clean.COMMON -> {
                taskPool.cleanPhoneSpace()
            }
            TaskScenes.Clean.RAM -> {
                taskPool.cleanPhoneRAM()
            }
            TaskScenes.Clean.APK -> {
                taskPool.cleanApk()
            }
            TaskScenes.Clean.RESIDUE -> {
                taskPool.cleanApk()
            }
            else -> {
                delay(500)
                true
            }
        }
    }

    suspend fun <T>scanTask(taskTag: String): Any {
        return when (taskTag) {
            TaskScenes.Scan.RAM -> {
                taskPool.scanRunningApp()
            }
            else -> {
                delay(500)

            }
        }
    }

    suspend fun cleanTask(taskTag: String): Boolean {
        return when (taskTag) {
            TaskScenes.Clean.COMMON -> {
                taskPool.cleanPhoneSpace()
            }
            TaskScenes.Clean.RAM -> {
                taskPool.cleanPhoneRAM()
            }
            TaskScenes.Clean.APK -> {
                taskPool.cleanApk()
            }
            TaskScenes.Clean.RESIDUE -> {
                taskPool.cleanApk()
            }
            else -> {
                delay(500)
                true
            }
        }
    }


}