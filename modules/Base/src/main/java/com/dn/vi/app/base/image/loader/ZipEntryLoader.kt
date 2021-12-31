package com.dn.vi.app.base.image.loader

import android.content.res.Resources
import android.util.Log
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import java.io.File
import java.io.InputStream
import java.util.zip.ZipFile

/**
 * 加载 zip里面的图片文件
 * Created by holmes on 20-1-5.
 */
class ZipEntryLoader : ModelLoader<ZipPic, InputStream> {

    companion object {
        const val TAG = "zipLoader"
    }

    override fun buildLoadData(
        model: ZipPic,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(ObjectKey(model), ZipPicDataFetcher(model))
    }

    override fun handles(model: ZipPic): Boolean {
        return true
    }


    /**
     * load a single data from zip file
     */
    class ZipPicDataFetcher(private val zipPic: ZipPic) : DataFetcher<InputStream> {

        private var openZip: ZipFile? = null
        private var data: InputStream? = null

        override fun getDataClass(): Class<InputStream> {
            return InputStream::class.java
        }

        override fun cleanup() {
            try {
                data?.close()
            } catch (e: Exception) {
            }
            try {
                openZip?.close()
            } catch (e: Exception) {
            }
        }

        override fun getDataSource(): DataSource {
            return DataSource.LOCAL
        }

        override fun cancel() {
        }

        override fun loadData(
            priority: Priority,
            callback: DataFetcher.DataCallback<in InputStream>
        ) {
            val zipFile = try {
                ZipFile(File(zipPic.filepath))
            } catch (e: Exception) {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "Failed to open zip", e)
                }
                callback.onLoadFailed(e)
                return
            }

            openZip = zipFile

            val found = try {
                zipFile.getEntry(zipPic.filename)
            } catch (e: Exception) {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "Failed to load ${zipPic.filename} from zip", e)
                }
                callback.onLoadFailed(e)
                return
            }

            if (found == null) {
                val msg = "not found ${zipPic.filename} in zip"
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, msg)
                }
                callback.onLoadFailed(Resources.NotFoundException(msg))
                return
            }

            val dataStream = try {
                zipFile.getInputStream(found)
            } catch (e: Exception) {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, " error to open stream ${zipPic.filename} from zip", e)
                }
                callback.onLoadFailed(e)
                return
            }

            data = dataStream

            callback.onDataReady(dataStream)
        }

    }

    class Factory : ModelLoaderFactory<ZipPic, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ZipPic, InputStream> {
            return ZipEntryLoader()
        }

        override fun teardown() {
        }

    }

}