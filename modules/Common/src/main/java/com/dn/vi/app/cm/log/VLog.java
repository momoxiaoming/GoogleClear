package com.dn.vi.app.cm.log;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.mars.xlog.Log;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 应用初始化时，掉用
 * {@link XLogInitializer#initializeXLog(context, namespace, debug)}
 * XLog wrap
 * Created by holmes on 2020/5/15.
 **/
@SuppressWarnings("JavadocReference")
public class VLog {

    // === 和XLog里面的定义一样 ===

    public static final int LEVEL_VERBOSE = Log.LEVEL_VERBOSE;
    public static final int LEVEL_DEBUG = Log.LEVEL_DEBUG;
    public static final int LEVEL_INFO = Log.LEVEL_INFO;
    public static final int LEVEL_WARNING = Log.LEVEL_WARNING;
    public static final int LEVEL_ERROR = Log.LEVEL_ERROR;
    public static final int LEVEL_FATAL = Log.LEVEL_FATAL;
    public static final int LEVEL_NONE = Log.LEVEL_NONE;

    // ===  ===

    public static String TAG_PREFIX = "Vi";
    private static Logger sDefaultLogger = null;
    private static HashMap<String, Logger> sScoped = new HashMap<>(32);
    private static HashMap<String, String> sScopeBuildCache = new HashMap<>(32);

    static final ReentrantReadWriteLock IMPL_LOCK = new ReentrantReadWriteLock();
    static Runnable sReInitialize;

    private VLog() {
    }

    /**
     * 跟 {@link XLogInitializer.initializeXLog} 一毛一样
     *
     * @param context
     * @param namespace
     * @param debug
     */
    public static void initializeXLog(Context context, String namespace, boolean debug) {
        final Runnable initIt = () -> {
            XLogInitializer.initializeXLog(context, namespace, debug);
            // 设置一下默认级别
            if (debug) {
                setLogLevel(LEVEL_DEBUG, true);
            } else {
                setLogLevel(LEVEL_INFO, true);
            }
        };
        initIt.run();
        sReInitialize = initIt;
    }

    /**
     * 重新初始化xlog。
     * 主要用在将xlog关闭后，再次开启。
     * 必须之前有调用过{@link #initializeXLog(Context, String, boolean)}。
     */
    static void reInitialize() {
        final Runnable initIt = sReInitialize;
        if (initIt != null) {
            initIt.run();
        }
    }

    /**
     * default is Level_NONE
     *
     * <p>
     * {@link Log.LEVEL_VERBOSE}
     * {@link Log.LEVEL_DEBUG}
     * {@link Log.LEVEL_INFO}
     * {@link Log.LEVEL_WARNING}
     * {@link Log.LEVEL_ERROR}
     * {@link Log.LEVEL_FATAL}
     * {@link Log.LEVEL_NONE}
     *
     * @param level
     * @param jni
     */
    public static void setLogLevel(final int level, final boolean jni) {
        Log.setLevel(level, jni);
    }

    public static int getLogLevel() {
        return Log.getLogLevel();
    }

    public synchronized static void setDefaultTag(String tag) {
        final String scopeTag = buildFinalScope(tag);
        if (sDefaultLogger == null ||
                (!sDefaultLogger.mTag.equals(scopeTag) && !TextUtils.isEmpty(scopeTag))) {
            sDefaultLogger = new Logger(scopeTag);
        }
    }

    public synchronized static void setDefaultLog(Logger log) {
        if (log != null) {
            sDefaultLogger = log;
        }
    }

    public static Logger getDefault() {
        final Logger l = sDefaultLogger;
        if (l == null) {
            setDefaultTag("Vig");
            return sDefaultLogger;
        } else {
            return l;
        }
    }

    /**
     * 生成一个独立 [tag] 的 Logger
     *
     * @param tag
     * @return
     */
    public static Logger getScopedLogger(String tag) {
        final String scopedTag = buildFinalScope(tag);
        Logger exists = sScoped.get(scopedTag);
        if (exists != null) {
            return exists;
        }
        synchronized (sScoped) {
            exists = sScoped.get(scopedTag);
            if (exists != null) {
                return exists;
            }

            // create a new one
            exists = new Logger(scopedTag);
            sScoped.put(scopedTag, exists);
            return exists;
        }
    }

    /**
     * {@link #getScopedLogger(String)}的别名。
     * 短一些。
     *
     * @param tag
     * @return
     */
    public static Logger scoped(String tag) {
        return getScopedLogger(tag);
    }

    private static String buildFinalScope(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return "";
        }
        final String cached = sScopeBuildCache.get(tag);
        if (TextUtils.isEmpty(cached)) {
            synchronized (sScopeBuildCache) {
                String scope = TAG_PREFIX + ":" + tag.trim();
                sScopeBuildCache.put(tag, scope);
                return scope;
            }
        }
        return cached;
    }


    // ==== Sugar ====

    public static void v(String msg) {
        final Logger def = getDefault();
        def.v(msg);
    }

    public static void d(String msg) {
        final Logger def = getDefault();
        def.d(msg);
    }

    public static void i(String msg) {
        final Logger def = getDefault();
        def.i(msg);
    }

    public static void w(String msg) {
        final Logger def = getDefault();
        def.w(msg);
    }

    public static void e(String msg) {
        final Logger def = getDefault();
        def.e(msg);
    }

    public static void printErrStackTrace(Throwable tr, final String format, final Object... obj) {
        final Logger def = getDefault();
        def.printErrStackTrace(tr, format, obj);
    }

    /**
     * 不建议使用，要tag，配合{@link #scoped(String)}来分组
     * @param tag
     * @param msg
     */
    @Deprecated()
    public static void v(String tag, String msg) {
        final Logger def = getDefault();
        def.v(msg);
    }

    /**
     * 不建议使用，要tag，配合{@link #scoped(String)}来分组
     * @param tag
     * @param msg
     */
    @Deprecated()
    public static void d(String tag, String msg) {
        final Logger def = getDefault();
        def.d("[%s] %s", tag, msg);
    }

    /**
     * 不建议使用，要tag，配合{@link #scoped(String)}来分组
     * @param tag
     * @param msg
     */
    @Deprecated()
    public static void i(String tag, String msg) {
        final Logger def = getDefault();
        def.i("[%s] %s", tag, msg);
    }

    /**
     * 不建议使用，要tag，配合{@link #scoped(String)}来分组
     * @param tag
     * @param msg
     */
    @Deprecated()
    public static void w(String tag, String msg) {
        final Logger def = getDefault();
        def.w("[%s] %s", tag, msg);
    }

    /**
     * 不建议使用，要tag，配合{@link #scoped(String)}来分组
     * @param tag
     * @param msg
     */
    @Deprecated()
    public static void e(String tag, String msg) {
        final Logger def = getDefault();
        def.e("[%s] %s", tag, msg);
    }

    // ==== ====

    /**
     * Logger
     */
    public static class Logger {

        private String mTag;

        private Logger(String tag) {
            this.mTag = tag;
        }

        public void v(String msg) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.v(mTag, msg);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }
        }

        public void d(String msg) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.d(mTag, msg);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }
        }

        public void i(String msg) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.i(mTag, msg);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }
        }

        public void w(String msg) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.w(mTag, msg);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }
        }

        public void e(String msg) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.e(mTag, msg);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }
        }

        // format
        public void v(final String format, final Object... obj) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.v(mTag, format, obj);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }
        }

        public void d(final String format, final Object... obj) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.d(mTag, format, obj);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }

        }

        public void i(final String format, final Object... obj) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.i(mTag, format, obj);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }
        }

        public void w(final String format, final Object... obj) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.w(mTag, format, obj);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }
        }

        public void e(final String format, final Object... obj) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.e(mTag, format, obj);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }
        }

        public void printErrStackTrace(Throwable tr, final String format, final Object... obj) {
            IMPL_LOCK.readLock().lock();
            try {
                Log.printErrStackTrace(mTag, tr, format, obj);
            } finally {
                IMPL_LOCK.readLock().unlock();
            }
        }
    }

}
