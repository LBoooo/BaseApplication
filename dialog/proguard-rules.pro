#---------dialog---------------------------------------------------------------------------------------------------------------------------#
-keep public class com.hinacle.appdialog.AppDialog { *; }
#---------dialog---------------------------------------------------------------------------------------------------------------------------#

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keep public class androidx.fragment.app.DialogFragment{
   public *;
   protected *;
   private *;
}