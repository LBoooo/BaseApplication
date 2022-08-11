#指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
#这句话能够使我们的项目混淆后产生映射文件# 包含有类名->混淆后类名的映射关系
-verbose
#指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
#不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify
#保留Annotation不混淆
-keepattributes Annotation,InnerClasses
#避免混淆泛型
-keepattributes Signature
#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
#指定混淆是采用的算法，后面的参数是一个过滤器
#这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/,!class/merging/



# 保留了继承自Activity、Application、Service、BroadcastReceiver、ContentProvider、BackupAgentHelper、
# Preference和ILicensingService  的子类。因为这些子类，都是可能被外部调用的。
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.app.FragmentActivity
-keep public class * extends android.support.v4.app.Fragmen
# 保留了含有native方法的类
-keepclasseswithmembernames class * {native <methods>;}

-keepattributes *Annotation*
# 保留构造函数从xml构造的类（一般为View的子类）
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
# 保留构造函数从xml构造的类（一般为View的子类）
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# 保护指定类的成员，如果此类受到保护他们会保护的更好
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
# 保留枚举类型中的values和valueOf静态方法
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# 保留继承Parcelable的跨进程数据类
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
## 保留反射中用到的类和方法，到时根据具体情况再改
#
## 反编译测试的时候有效，运行时也与未混淆情况一样
#-keepclassmembers class 包名.** {
#   public *;
#   protected *;
#   private *;
#}

##保留继承的
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.support.v7.**
#-keep public class * extends android.support.annotation.**
##保留R下面的资源
##-keep class .R$ {;}
##保留本地native方法不被混淆
##-keepclasseswithmembernames class * { native ;}
##保留在Activity中的方法参数是view的方法，
##这样以来我们在layout中写的onClick就不会被影响
##-keepclassmembers class * extends android.app.Activity{ public void *(android.view.View);}
##保留枚举类不被混淆
#-keepclassmembers enum * { public static **[] values(); public static ** valueOf(java.lang.String);}
##保留我们自定义控件(继承自View)不被混淆
##-keep public class * extends android.view.View{ *** get(); void set(***); public (android.content.Context); public (android.content.Context, android.util.AttributeSet); public (android.content.Context, android.util.AttributeSet, int);}
##保留Parcelable序列化类不被混淆
##-keep class * implements android.os.Parcelable { public static final android.os.Parcelable$Creator *;}
##保留Serializable序列化的类不被混淆
##-keepclassmembers class * implements java.io.Serializable { static final long serialVersionUID; private static final java.io.ObjectStreamField[] serialPersistentFields; !static !transient ; !private ; !private ; private void writeObject(java.io.ObjectOutputStream); private void readObject(java.io.ObjectInputStream); java.lang.Object writeReplace(); java.lang.Object readResolve();}
#
##webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}


#---------kotlinx-serialization---------------------------------------------------------------------------------------------------------------------------#

# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Serializer for classes with named companion objects are retrieved using `getDeclaredClasses`.
# If you have any, uncomment and replace classes with those containing named companion objects.
#-keepattributes InnerClasses # Needed for `getDeclaredClasses`.
#-if @kotlinx.serialization.Serializable class
#com.example.myapplication.HasNamedCompanion, # <-- List serializable classes with named companions.
#com.example.myapplication.HasNamedCompanion2
#{
#    static **$* *;
#}
#-keepnames class <1>$$serializer { # -keepnames suffices; class is kept when serializer() is kept.
#    static <1>$$serializer INSTANCE;
#}

-keepattributes Annotation, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-dontnote kotlinx.serialization.SerializationKt

# Keep Serializers

-keep,includedescriptorclasses class com.hinacle.base.**$$serializer { *; }
-keepclassmembers class com.hinacle.base.** {
    *** Companion;
}
-keepclasseswithmembers class com.hinacle.base.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# When kotlinx.serialization.json.JsonObjectSerializer occurs

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
#---------kotlinx-serialization---------------------------------------------------------------------------------------------------------------------------#

