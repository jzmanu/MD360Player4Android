package com.asha.vrlib.strategy;


import android.content.Context;

import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.common.MDGLHandler;
import com.asha.vrlib.common.MDMainHandler;

import java.util.Arrays;

import static com.asha.vrlib.common.VRUtil.checkMainThread;

/**
 * add jzman 20200524
 * 抽象的Mode管理器应该有什么功能，这些功能由具体的管理的自己实现。
 * 1. 创建IModeStrategy
 * 2. 获取当前管理器所有的Mode
 * 3. 切换Mode
 * 4. 开启
 * 5. 关闭
 * 6. 获取IModeStrategy
 *
 * Created by hzqiujiadi on 16/3/19.
 * hzqiujiadi ashqalcn@gmail.com
 */
public abstract class ModeManager<T extends IModeStrategy> {
    private int mMode;
    private T mStrategy;
    private MDVRLibrary.INotSupportCallback mCallback;
    private MDGLHandler mGLHandler;

    public ModeManager(int mode, MDGLHandler handler) {
        this.mGLHandler = handler;
        this.mMode = mode;
    }

    /**
     * must call after new instance
     * @param context context
     */
    public void prepare(Context context, MDVRLibrary.INotSupportCallback callback){
        mCallback = callback;
        initMode(context,mMode);
    }

    abstract protected T createStrategy(int mode);

    abstract protected int[] getModes();

    private void initMode(Context context, final int mode){
        if (mStrategy != null){
            off(context);
        }
        mStrategy = createStrategy(mode);
        if (!mStrategy.isSupport(context)){
            MDMainHandler.sharedHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onNotSupport(mode);
                    }
                }
            });
            return;
        }

        // t
        on(context);
    }

    public void switchMode(final Context context){
        int[] modes = getModes();
        int mode = getMode();
        int index = Arrays.binarySearch(modes, mode);
        int nextIndex = (index + 1) %  modes.length;
        int nextMode = modes[nextIndex];

        switchMode(context, nextMode);
    }

    public void switchMode(final Context context, final int mode){
        if (mode == getMode()) return;
        mMode = mode;

        initMode(context, mMode);
    }

    public void on(final Context context) {
        checkMainThread("strategy on must call from main thread!");

        final T tmpStrategy = mStrategy;
        if (tmpStrategy.isSupport(context)){
            getGLHandler().post(new Runnable() {
                @Override
                public void run() {
                    tmpStrategy.turnOnInGL(context);
                }
            });
        }
    }

    public void off(final Context context) {
        checkMainThread("strategy off must call from main thread!");

        final T tmpStrategy = mStrategy;
        if (tmpStrategy.isSupport(context)){
            getGLHandler().post(new Runnable() {
                @Override
                public void run() {
                    tmpStrategy.turnOffInGL(context);
                }
            });
        }
    }

    protected T getStrategy() {
        return mStrategy;
    }

    public int getMode() {
        return mMode;
    }

    public MDGLHandler getGLHandler() {
        return mGLHandler;
    }
}
