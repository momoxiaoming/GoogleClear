package com.dn.vi.app.repo.kv

import android.content.Context
import com.dn.vi.app.cm.log.VLog
import io.reactivex.rxjava3.core.Observable
import okio.buffer
import okio.sink
import okio.source
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileFilter
import java.io.FileOutputStream
import java.util.concurrent.Callable
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * 用于把数据库保存一份, 导出来
 *
 * 正常导出，会返回一个 path
 * Created by holmes on 2020/7/6.
 **/
class KvDump(private val context: Context) : Callable<String>, FileFilter {

    private val dbDir: File by lazy {
        val fakeDb = context.getDatabasePath("kvdump")
        fakeDb.parentFile
    }

    private fun copyDatabase(): List<File> {
        val kvdb = dbDir.listFiles(this)
        if (kvdb.isNullOrEmpty()) {
            return emptyList()
        }
        return kvdb.toList()
    }

    private fun compressDatabase(file: List<File>): String {
        val export = File(context.externalCacheDir, "kvdp.dat")
        ZipOutputStream(BufferedOutputStream(FileOutputStream(export))).use { zip ->
            zipFiles(zip, file, "")
        }
        return export.absolutePath
    }

    override fun call(): String {
        val copyDatabase = copyDatabase()
        if (!copyDatabase.isNullOrEmpty()) {
            return compressDatabase(copyDatabase)
        }
        return ""
    }

    fun rx(): Observable<String> {
        return Observable.create { emitter ->
            try {
                val path = call()
                if (!emitter.isDisposed) {
                    if (!path.isNullOrEmpty()) {
                        emitter.onNext(path)
                        emitter.onComplete()
                    } else {
                        emitter.onError(RuntimeException("dump path empty"))
                    }
                }
            } catch (e: Exception) {
                if (!emitter.isDisposed) {
                    emitter.onError(e)
                }
            }
        }
    }

    override fun accept(pathname: File?): Boolean {
        pathname ?: return false
        if (pathname.isDirectory) {
            return false
        }
        return pathname.name.startsWith(KvLite.databaseName)
    }


    private fun zipFiles(zipOut: ZipOutputStream, sourceFile: List<File>, parentDirPath: String) {

        for (f in sourceFile) {

            if (f.isDirectory) {
                val entry = ZipEntry(f.name + File.separator)
                entry.time = f.lastModified()
                entry.isDirectory
                entry.size = f.length()

                VLog.scoped("kvlite").i("dump archive add dir: ${f.name}")
                zipOut.putNextEntry(entry)

                //Call recursively to add files within this directory
                // 单一层级
                zipFiles(zipOut, f.listFiles()?.toList() ?: emptyList(), f.name)
            } else {

                if (!f.endsWith(".zip")) {
                    //If folder contains a file with extension ".zip", skip it
                    f.source().buffer().use { fBuff ->
                        val path = if (parentDirPath.isNullOrEmpty()) {
                            f.name
                        } else {
                            parentDirPath + File.separator + f.name
                        }
                        VLog.scoped("kvlite").i("dump archive add file: ${f.name}")

                        val entry = ZipEntry(path)
                        entry.time = f.lastModified()
                        entry.isDirectory
                        entry.size = f.length()
                        zipOut.putNextEntry(entry)

                        fBuff.readAll(zipOut.sink())
                    }
                } else {
                    zipOut.closeEntry()
                    zipOut.close()
                }

            }
        }
    }
}