package com.dn.vi.app.base

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dn.vi.app.repo.kv.KvLite
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class KvLiteTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.dn.vi.app.base.test", appContext.packageName)
    }

    @Test
    fun testStringHash() {
        val keys: List<String> = listOf(
            "asfadsfasdf",
            "12319283791",
            "我的",
            "httpsdfs"
        )

        keys.forEach { k ->
            val hash = KvLite.buildHash(k)
            println("[$k](${k.hashCode()} $hash)")
        }
    }

    @Test
    fun testKvRepo() {
        val pair = "int" to 123
        KvLite.putObj(pair.first, pair.second)

        KvLite.async.getKv(pair.first)
            .subscribeBy {
                println("[$pair] $it")
            }
    }

}
