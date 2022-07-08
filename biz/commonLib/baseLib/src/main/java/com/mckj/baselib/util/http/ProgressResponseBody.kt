package com.mckj.baselib.util.http

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val listener: ProgressListener,
) :
    ResponseBody() {

    private var mBufferedSource: BufferedSource? = null

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun source(): BufferedSource {
        var buffer = mBufferedSource
        if (buffer == null) {
            buffer = source(responseBody.source()).buffer()
            buffer.request(Long.MAX_VALUE)
            mBufferedSource = buffer
        }
        return buffer
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {

            var totalSize = 0L
            var downloadSize = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                val read = super.read(sink, byteCount)
                val updateSize: Long = if (read == -1L) {
                    0L
                } else {
                    read
                }
                totalSize = responseBody.contentLength()
                downloadSize += updateSize
                listener.update(updateSize, downloadSize, totalSize)
                return read
            }
        }
    }

}