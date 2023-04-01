
#
#-------------------------------------------基本不用动区域----------------------------------------------
#
#
# -----------------------------基本 -----------------------------
#

# 指定代码的压缩级别 0 - 7(指定代码进行迭代优化的次数，在Android里面默认是5，这条指令也只有在可以优化时起作用。)
-optimizationpasses 5
# 混淆时不会产生形形色色的类名(混淆时不使用大小写混合类名)
-dontusemixedcaseclassnames
# 指定不去忽略非公共的库类(不跳过library中的非public的类)
-dontskipnonpubliclibraryclasses
# 指定不去忽略包可见的库类的成员
-dontskipnonpubliclibraryclassmembers
#不进行优化，建议使用此选项，
-dontoptimize
 # 不进行预校验,Android不需要,可加快混淆速度。
-dontpreverify
# 屏蔽警告
-ignorewarnings
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 保护代码中的Annotation不被混淆
-keepattributes *Annotation*
# 避免混淆泛型, 这在JSON实体映射时非常重要
-keepattributes Signature
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
 #优化时允许访问并修改有修饰符的类和类的成员，这可以提高优化步骤的结果。
# 比如，当内联一个公共的getter方法时，这也可能需要外地公共访问。
# 虽然java二进制规范不需要这个，要不然有的虚拟机处理这些代码会有问题。当有优化和使用-repackageclasses时才适用。
#指示语：不能用这个指令处理库中的代码，因为有的类和类成员没有设计成public ,而在api中可能变成public
-allowaccessmodification
#当有优化和使用-repackageclasses时才适用。
-repackageclasses ''
 # 混淆时记录日志(打印混淆的详细信息)
 # 这句话能够使我们的项目混淆后产生映射文件
 # 包含有类名->混淆后类名的映射关系
-verbose

#
# ----------------------------- 默认保留 -----------------------------
#
#----------------------------------------------------
# 保持哪些类不被混淆
#继承activity,application,service,broadcastReceiver,contentprovider....不进行混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep class android.support.** {*;}## 保留support下的所有类及其内部类
-keep public class com.zong.call.module.** {*;}

-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.zong.call.utils.HttpHelper
-keep public class com.android.network.sdk.okhttp.RetrofitApi
-keep public class com.zong.call.utils.ToPinYin
#表示不混淆上面声明的类，最后这两个类我们基本也用不上，是接入Google原生的一些服务时使用的。
#----------------------------------------------------

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends com.android.network.BaseBean.**
-keep public class * extends android.support.annotation.**


#表示不混淆任何包含native方法的类的类名以及native方法名，这个和我们刚才验证的结果是一致
-keepclasseswithmembernames class * {
    native <methods>;
}


#这个主要是在layout 中写的onclick方法android:onclick="onClick"，不进行混淆
#表示不混淆Activity中参数是View的方法，因为有这样一种用法，在XML中配置android:onClick=”buttonClick”属性，
#当用户点击该按钮时就会调用Activity中的buttonClick(View view)方法，如果这个方法被混淆的话就找不到了
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#表示不混淆枚举中的values()和valueOf()方法，枚举我用的非常少，这个就不评论了
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#表示不混淆任何一个View中的setXxx()和getXxx()方法，
#因为属性动画需要有相应的setter和getter的方法实现，混淆了就无法工作了。
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#表示不混淆Parcelable实现类中的CREATOR字段，
#毫无疑问，CREATOR字段是绝对不能改变的，包括大小写都不能变，不然整个Parcelable工作机制都会失败。
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# 这指定了继承Serizalizable的类的如下成员不被移除混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# 保留R下面的资源
#-keep class **.R$* {
# *;
#}
#不混淆资源类下static的
-keepclassmembers class **.R$* {
    public static <fields>;
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 指定一个文本文件用来生成混淆后的名字。默认情况下，混淆后的名字一般为 a、b、c 这种。
# 通过使用配置的字典文件，可以使用一些非英文字符做为类名。成员变量名、方法名。字典文件中的空格，标点符号，重复的词，还有以'#'开头的行都会被忽略。
# 需要注意的是添加了字典并不会显著提高混淆的效果，只不过是更不利与人类的阅读。正常的编译器会自动处理他们，并且输出出来的jar包也可以轻易的换个字典再重新混淆一次。
# 最有用的做法一般是选择已经在类文件中存在的字符串做字典，这样可以稍微压缩包的体积。
# 查找了字典文件的格式：一行一个单词，空行忽略，重复忽略
-obfuscationdictionary proguard-dictionary.txt

# 指定一个混淆类名的字典，字典格式与 -obfuscationdictionary 相同
-classobfuscationdictionary proguard-dictionary.txt
# 指定一个混淆包名的字典，字典格式与 -obfuscationdictionary 相同
-packageobfuscationdictionary proguard-dictionary.txt

#
#----------------------------- WebView(项目中没有可以忽略) -----------------------------
#
#webView需要进行特殊处理
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#在app中与HTML5的JavaScript的交互进行特殊处理
#我们需要确保这些js要调用的原生方法不能够被混淆，于是我们需要做如下处理：
-keepclassmembers class com.ljd.example.JSInterface {
    <methods>;
}

#
#---------------------------------实体类---------------------------------
#--------(实体Model不能混淆，否则找不到对应的属性获取不到值)-----
#
-dontwarn com.suchengkeji.android.confusiondemo.md.**
#对含有反射类的处理
-keep class com.suchengkeji.android.confusiondemo.md.** { *; }
#
# ----------------------------- 其他的 -----------------------------
#
# 删除代码中Log相关的代码
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**


#
# ----------------------------- 第三方 -----------------------------
#
-dontwarn com.orhanobut.logger.**
-keep class com.orhanobut.logger.**{*;}
-keep interface com.orhanobut.logger.**{*;}

-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}
-keep interface com.google.gson.**{*;}
#        。。。。。。
#####################################
-keep class com.umeng.** {*;}
-keep class net.sourceforge.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.zui.** {*;}
-keep class com.miui.** {*;}
-keep class com.heytap.** {*;}
-keep class com.zong.call.module.** {*;}
-keep class a.** {*;}
-keep class com.vivo.** {*;}
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}


-keep class com.umeng.** {*;}

-keep class com.uc.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.zui.** {*;}
-keep class com.miui.** {*;}
-keep class com.heytap.** {*;}
-keep class a.** {*;}
-keep class com.vivo.** {*;}
-keep class com.iflytek.**{*;}
-dontwarn com.iflytek.speech.**

-keepattributes Signature

-keep class com.iflytek.speech.**{*;}

######################################友盟 start######################################
-keep class com.umeng.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#SDK 9.2.4及以上版本自带oaid采集模块，不再需要开发者再手动引用oaid库，所以可以不添加这些混淆
-keep class com.zui.**{*;}
-keep class com.miui.**{*;}
-keep class com.heytap.**{*;}
-keep class a.**{*;}
-keep class com.vivo.**{*;}
######################################友盟 end######################################

######################################com.github.addappcn:android-pickers:2.0.0 start######################################
-keepattributes InnerClasses,Signature
-keepattributes *Annotation*

-keep class cn.addapp.pickers.entity.** { *;}
######################################com.github.addappcn:android-pickers:2.0.0 end######################################

-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
## ---------Retrofit混淆方法---------------
# Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

# okhttp
-dontwarn okio.**

-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Gson
-keep class com.example.testing.retrofitdemo.bean.**{*;} # 自定义数据模型的bean目录
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
# okhttp-dontwarn okio.

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod


######################################广告==######################################
# 屏蔽警告
-ignorewarnings
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 保护代码中的Annotation不被混淆
-keepattributes *Annotation*
# 避免混淆泛型, 这在JSON实体映射时非常重要
-keepattributes Signature
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
 #优化时允许访问并修改有修饰符的类和类的成员，这可以提高优化步骤的结果。
# 比如，当内联一个公共的getter方法时，这也可能需要外地公共访问。
# 虽然java二进制规范不需要这个，要不然有的虚拟机处理这些代码会有问题。当有优化和使用-repackageclasses时才适用。
#指示语：不能用这个指令处理库中的代码，因为有的类和类成员没有设计成public ,而在api中可能变成public
-allowaccessmodification
#当有优化和使用-repackageclasses时才适用。
 # 混淆时记录日志(打印混淆的详细信息)
 # 这句话能够使我们的项目混淆后产生映射文件
 # 包含有类名->混淆后类名的映射关系
-verbose

# OAID混淆
-keep class XI.CA.XI.**{*;}
-keep class XI.K0.XI.**{*;}
-keep class XI.XI.K0.**{*;}
-keep class XI.vs.K0.**{*;}
-keep class XI.xo.XI.XI.**{*;}
-keep class com.asus.msa.SupplementaryDID.**{*;}
-keep class com.asus.msa.sdid.**{*;}
-keep class com.bun.lib.**{*;}
-keep class com.bun.miitmdid.**{*;}
-keep class com.huawei.hms.ads.identifier.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class org.json.**{*;}
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}

# 广点通广告平台混淆
-keep class com.qq.e.** {public protected *;}
-keep class MTT.ThirdAppInfoNew {*;}
-keep class com.tencent.** {*;}

# 头条广告平台混淆
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}

# MobiusAd平台混淆
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName *;
}

# 百度广告平台混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.baidu.mobads.** { *; }
-keep class com.baidu.mobad.** { *; }
-keep class com.bun.miitmdid.core.** {*;}

# Smaato广告平台混淆
-keep public class com.smaato.sdk.** { *; }
-keep public interface com.smaato.sdk.** { *; }

# 芒果广告平台混淆
-keep class android.support.v4.** { public *; }
-keep class android.support.v7.** { public *; }
-keep class * implements java.io.Serializable {*;}
-keep class com.hunantv.media.** { *;}
-keep class com.mgmi.** { *;}
-keep class com.mgadplus.** { *;}
-dontwarn com.hmt.analytics.**
-dontwarn org.apaches.commons.codec.**
-keep class com.hmt.analytics.**{*; }
-keep class org.apaches.commons.codec.**{*; }
# freco
-dontwarn com.facebook.**
-keep enum com.facebook.**
-keep public interface com.facebook.**
-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
 @com.facebook.common.internal.DoNotStrip *;
}
-dontwarn org.eclipse.**
-keep class org.eclipse.** { *;}
-dontwarn com.squareup.**
-keep class com.squareup.** { *;}
-keep public class com.mi.ad.sdk.**{*;}
-keep public class com.doman.core.**{*;}
-keep public class com.core.cell.** {*;}
# android
-keep class android.**{*;}
-keep @interface system.** {*;}
-keepclassmembers class system.**{ public *;}
-dontwarn android.**
-dontwarn com.android.**
-dontwarn system.**
-keep @interface com.core.cell.helper.Keep {*;}
-keep @interface com.android.a.a.**{*;}
-keep class io.reactivex.**{*;}
-keep class com.github.megatronking.stringfog.**{*;}
-keep @interface com.github.megatronking.stringfog.**{*;}
# 华为平台混淆
-keep class com.huawei.openalliance.ad.** { *; }
-dontwarn com.huawei.openalliance.ad.activity.PPSActivity

# mimo 平台混淆
-keep class com.miui.zeus.mimo.sdk.* { *; }
-keep class com.miui.analytics.** { *; }
-keep class com.xiaomi.analytics.* { public protected *; }
-keep class * extends android.os.IInterface{ *; }
-keep class me.pqpo.librarylog4a.LogBuffer  { *; }
#华为
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.agconnect.**{*;}
-keep interface com.huawei.hms.analytics.type.HAEventType{*;}
-keep interface com.huawei.hms.analytics.type.HAParamType{*;}