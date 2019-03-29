package com.lcgsen.master.xposed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
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
        if (lpparam.packageName.contains("com.eg.android.AlipayGphone")) {
            XposedBridge.log("Loaded App: " + lpparam.packageName);
            XposedBridge.log("Powered by Sena");

            //Pay Diamond
            XposedHelpers.findAndHookMethod(
                    "com.alipay.mobilegw.biz.shared.processer.login.UserLoginResult",
                    lpparam.classLoader,
                    "getExtResAttrs",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("Now, let's install B...");
                            XSharedPreferences pre = new XSharedPreferences("com.lcgsen.master.xposed", "prefs");
                            pre.makeWorldReadable();
                            String enabledBackground = pre.getString("enableBackground", "true");
                            XposedBridge.log("enableBackground value: " + enabledBackground);

                            if (!enabledBackground.equals("true")) {
                                XposedBridge.log("Install B is failed! Not enabled.");
                                return;
                            }

                            Map<String, String> result = (Map<String, String>) param.getResult();
                            if (result.containsKey("memberGrade")) {
                                // golden 黄金会员  diamond 钻石会员
                                XposedBridge.log("Original member grade: " + result.get("memberGrade"));
                                XposedBridge.log("Putting \"diamond\" into dict...");
                                result.put("memberGrade", "diamond");
                                XposedBridge.log("Member grade changed to: 钻石会员");
                            } else {
                                XposedBridge.log("Can not get the member grade in return value...WTF?");
                            }
                        }
                    });

            XposedBridge.log("Hook function was executed.");
        } else if (lpparam.packageName.contains("com.tencent.mm")) {
            initWechat(lpparam);
        }

    }

    private void initWechat(final XC_LoadPackage.LoadPackageParam paramLoadPackageParam) {
    }
}