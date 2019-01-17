package com.lcgsen.master.xposed;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class TargetHook implements IXposedHookLoadPackage {
    private static List<String> hostAppPackages = new ArrayList<>();

    static {
        XposedBridge.log("lcgsen-start---------");
        // TODO: Add the package name of application your want to hook!
        hostAppPackages.add("com.eg.android.AlipayGphone");
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log(lpparam.packageName);
        XposedBridge.log("load alipay----");
        XposedBridge.log("load alipay----");
        XposedBridge.log("load alipay----");
        XposedBridge.log("load alipay----");
        XposedBridge.log("load alipay----");
        XposedBridge.log("load alipay----");
        XposedBridge.log("load alipay----");
        if (hostAppPackages.contains(lpparam.packageName)) {
            XposedBridge.log("load alipay----");
            ClassLoader classLoader = lpparam.classLoader;
            Class<?> aClass = classLoader.loadClass("com.alipay.android.render.engine.viewbiz.AssetsHeaderV2View");
            Class<?> aClass2 = classLoader.loadClass("com.alipay.android.render.engine.model.AssetsCardModel");
            if (aClass != null) {
                XposedHelpers.findAndHookMethod(aClass, "setData", aClass2, boolean.class, boolean.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Object arg = param.args[0];
                        try {
                            Log.w("czc", arg.getClass().getField("latestTotalView").get(arg).toString());
                            arg.getClass().getField("latestTotalView").set(arg, "1000000.00");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });
            }
        }
    }
}