package com.dn.vi.app.cm.log;

import android.util.Log;


/**
 * Created by wensefu on 2017/6/24.
 */

public class DebugLog {

    static String className;
    static String methodName;
    static int lineNumber;

    private static boolean log_enable;

    private DebugLog() {
        /* Protect from instantiations */
    }

    public static void setLogEnable(boolean enable) {
        log_enable = enable;
    }

    public static boolean isDebuggable() {
        return log_enable;
    }

    private static String createLog(String log) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append("]");
        buffer.append(log);

        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(Throwable e) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(""), e);
    }

    public static void e(String msg,Throwable e) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(msg), e);
    }

    public static void e(String message) {
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void i(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d() {
        d("");
    }

    public static void d(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void d(Throwable e) {
        d("", e);
    }

    public static void d(String message, Throwable e) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message), e);
    }

    public static void v(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

    public static void d(String tag, String msg) {
        if (isDebuggable()) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable e) {
        if (isDebuggable()) {
            Log.d(tag, msg, e);
        }
    }
}
