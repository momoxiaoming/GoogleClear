package com.dn.vi.app.cm.http.logging;


import com.dn.vi.app.cm.log.VLog;

import okhttp3.internal.platform.Platform;

/**
 * Xlog impl
 */
public class X implements Logger {


    @Override
    public void log(int level, String tag, String msg) {
        final VLog.Logger log = VLog.scoped("http:" + tag);
        switch (level) {
            case Platform.INFO:
                log.i(msg);
                break;
            default:
                log.w(msg);
                break;
        }
    }

}
