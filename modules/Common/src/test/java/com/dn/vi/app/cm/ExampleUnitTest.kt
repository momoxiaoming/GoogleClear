package com.dn.vi.app.cm

import com.dn.vi.app.cm.utils.ProcessUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_processInfo() {
        val processName = ProcessUtils.currentProcessName
        print(processName)
        assertFalse(processName.isNullOrEmpty())
        assertFalse(ProcessUtils.isRemoteProcess(processName))
        assertTrue(ProcessUtils.getProcessScope(processName).isNullOrEmpty())
    }
}
