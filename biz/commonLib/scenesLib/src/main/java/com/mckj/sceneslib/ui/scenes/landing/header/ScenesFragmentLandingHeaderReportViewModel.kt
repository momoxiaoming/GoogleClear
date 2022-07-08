package com.mckj.sceneslib.ui.scenes.landing.header

import androidx.lifecycle.MutableLiveData
import com.dn.vi.app.repo.kv.KvSp
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.R

class ScenesFragmentLandingHeaderReportViewModel : AbstractViewModel() {

    var downloadNum = 0L
        set(value) {
            field = value
            configHistoryTop(value)
        }

    private val musicSize = 2 * 1024 * 1024//2M
    private val seriesSize = musicSize * 150//300M
    private val movieSize = musicSize * 400//800M

    private fun getTimeStrByDownloadSize(downloadSize: Long): String {
        return if (downloadNum <= 0) {
            ResourceUtil.getString(R.string.scenes_more_than_day)
        } else {
            val downloadSec = downloadSize / downloadNum
            getTimeStrBySec(downloadSec)
        }
    }

    fun getSeriesTimeStr(): String {
        return getTimeStrByDownloadSize(seriesSize.toLong())
    }

    fun getMovieTimeStr(): String {
        return getTimeStrByDownloadSize(movieSize.toLong())
    }

    fun getMusicTimeStr(): String {
        return getTimeStrByDownloadSize(musicSize.toLong())
    }

    private fun getTimeStrBySec(sec: Long): String {
        if (sec < 1) {
            return ResourceUtil.getString(R.string.scenes_less_1s)
        }

        val min = sec / 60
        if (min < 1) {
            return String.format(ResourceUtil.getString(R.string.scenes_x_sec),sec)
        }

        val h = min / 60
        if (h < 1) {
            return String.format(ResourceUtil.getString(R.string.scenes_x_min),min)
        }

        val d = h / 24
        return if (d < 1) {
            String.format(ResourceUtil.getString(R.string.scenes_x_hour),h)
        } else {
            String.format(ResourceUtil.getString(R.string.scenes_x_day),d)
        }
    }

    val historyTopData: MutableLiveData<Long> = MutableLiveData()

    companion object {
        const val HISTORY = "historyTopNetSpeed"
    }
    /**
     * 配置历史最高网速
     */
    private fun configHistoryTop(downloadNum: Long) {
        var historyTop = KvSp.getLong(HISTORY, 0)
        if (historyTop <= downloadNum) {
            KvSp.putLong(HISTORY, downloadNum)
            historyTop = downloadNum
        }
        historyTopData.value = historyTop
    }
}