#
-printmapping build/release-mapping.txt
#
#-dontnote **
-dontnote androidx.compose.foundation.text.selection.SelectionMode
-dontnote androidx.compose.ui.text.platform.ReflectionUtil$findAssignableField$result$1
-dontnote androidx.lifecycle.Lifecycle$Event
-dontnote androidx.lifecycle.Lifecycle$State
-dontnote androidx.savedstate.Recreator
-dontnote androidx.savedstate.SavedStateRegistry
-dontnote androidx.lifecycle.viewmodel.internal.JvmViewModelProviders

-dontnote kotlinx.serialization.internal.PlatformKt

-dontnote org.apache.log4j.**
-dontnote org.apache.logging.log4j.**
-dontnote jakarta.activation.**
-dontnote jakarta.xml.bind.**
-dontnote ch.qos.logback.core.joran.conditional.**
-dontnote org.slf4j.helpers.**

-dontnote org.jooq.**

-dontwarn ch.qos.logback.core.boolex.**
-dontwarn ch.qos.logback.core.joran.conditional.**

-dontwarn org.apache.logging.slf4j.**
-dontwarn com.lmax.**
-dontwarn com.fasterxml.**
-dontwarn org.osgi.framework.**
-dontwarn com.google.errorprone.annotations.**
-dontwarn edu.umd.cs.findbugs.annotations.**
-dontwarn javax.jms.**
-dontwarn javax.mail.**
-dontwarn org.zeromq.**
-dontwarn org.apache.kafka.**
-dontwarn org.apache.commons.compress.**
-dontwarn org.jctools.queues.**
-dontwarn com.conversantmedia.util.concurrent.**
-dontwarn org.apache.logging.log4j.core.async.**
-dontwarn org.apache.commons.csv.**
-dontwarn com.fasterxml.jackson.databind.**
-dontwarn aQute.bnd.annotation.spi.**
-dontwarn org.fusesource.jansi.**
-dontwarn org.codehaus.stax2.**
-dontwarn org.apache.logging.log4j.core.config.plugins.**
-dontwarn org.apache.logging.log4j.core.jackson.**
-dontwarn org.apache.logging.log4j.core.layout.JacksonFactory$Log4jXmlPrettyPrinter
-dontwarn javax.activation.DataSource
-dontwarn org.apache.logging.log4j.util.ServiceLoaderUtil


-dontwarn org.apache.log4j.**
-dontwarn org.apache.logging.log4j.**
-dontwarn org.apache.log.**

-dontwarn javax.annotation.**
-dontwarn org.jetbrains.annotations.**
-dontwarn jakarta.persistence.**
-dontwarn org.slf4j.**



#Not using Servlet
-dontwarn jakarta.servlet.**

#Not using Glassfish
-dontwarn org.glassfish.**


#Not using jakarta.mail - referenced by logback
-dontwarn jakarta.mail.**


-keepattributes *Annotation*
-keepattributes Signature

#Kotlinx Serialization rules as per https://github.com/Kotlin/kotlinx.serialization/blob/master/rules/common.pro
# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

-keep class org.apache.logging.log4j.** { *; }
-keepclassmembers class org.apache.logging.log4j.** { *; }
-keep class ch.qos.logback.** { *; }
-keepclassmembers class ch.qos.logback.** { *; }
-keepclassmembers class org.slf4j.impl.** { *; }


-keep class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keep class kotlinx.coroutines.swing.SwingDispatcherFactory { *; }
-keepclassmembers enum * {
    public *;
}

-keep class org.sqlite.** { *; }
-keep class org.sqlite.database.** { *; }

-keep, includedescriptorclasses interface org.jooq.** { <methods>; }
-keep, includedescriptorclasses class org.jooq.** { *; }

-keep class jakarta.activation.CommandInfo$Beans { <init>(); }

