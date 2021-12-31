package com.dn.vi.app.cm.log;

import android.content.Context;

import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.io.File;

/**
 * 接入 xlog.
 * <p>
 * 参考 https://github.com/Tencent/mars
 * Created by holmes on 2020/5/15.
 **/
public class XLogInitializer {

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
    }

    public static void initializeXLog(Context context, String namespace, boolean debug) {
        new XLogInitializer(context, namespace, debug);
    }

    private XLogInitializer(Context context, String namespace, boolean debug) {
        VLog.IMPL_LOCK.writeLock().lock();
        try {
            final File cacheDir = context.getExternalCacheDir();
            final File logPath = new File(cacheDir, "logs");

            // this is necessary, or may crash for SIGBUS
            final String cachePath = context.getFilesDir() + "/xlog";

            //init xlog
            final int maxAliveCacheDays = 15;
            final Xlog.XLogConfig logConfig = new Xlog.XLogConfig();
            logConfig.mode = Xlog.AppednerModeAsync;
            logConfig.logdir = logPath.getAbsolutePath();
            logConfig.nameprefix = namespace;
            logConfig.compressmode = Xlog.ZLIB_MODE;
            logConfig.compresslevel = 0;
            logConfig.cachedir = cachePath;
            logConfig.cachedays = 0;

            if (debug) {
                logConfig.level = Xlog.LEVEL_DEBUG;
            } else {
                logConfig.level = Xlog.LEVEL_INFO;
            }

            Xlog impl = new Xlog();
            impl.setMaxAliveTime(0, maxAliveCacheDays * 24L * 60L * 60L);
            Log.setLogImp(impl);
            Log.setConsoleLogOpen(debug);
            appenderOpen(logConfig);
        } finally {
            VLog.IMPL_LOCK.writeLock().unlock();
        }

    }

    private void appenderOpen(Xlog.XLogConfig logConfig) {
        Log.appenderOpen(
                logConfig.level,
                logConfig.mode,
                logConfig.cachedir,
                logConfig.logdir,
                logConfig.nameprefix,
                logConfig.cachedays
        );
    }


}
