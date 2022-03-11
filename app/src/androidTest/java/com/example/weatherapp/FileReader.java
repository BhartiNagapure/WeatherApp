package com.example.weatherapp;

import android.app.Instrumentation;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.Intrinsics;

public final class FileReader {
    @NotNull
    public static final FileReader INSTANCE;

    @NotNull
    public final String readStringFromFile(@NotNull String fileName) {
        Intrinsics.checkNotNullParameter(fileName, "fileName");

        try {
            Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

            Context context = instrumentation.getTargetContext();
            context = context.getApplicationContext();


            InputStream stream = context.getAssets().open(fileName);
            InputStream inputStream = stream;

            StringBuilder builder = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            Iterable iterable = (Iterable) TextStreamsKt.readLines((Reader)reader);
            Iterator var7 = iterable.iterator();

            while(var7.hasNext()) {
                Object element$iv = var7.next();
                String it = (String)element$iv;
                builder.append(it);
            }

            String var14 = builder.toString();
            Intrinsics.checkNotNullExpressionValue(var14, "builder.toString()");
            return var14;
        } catch (IOException var11) {
            try {
                throw (Throwable)var11;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return fileName;
    }

    private FileReader() {
    }

    static {
        FileReader var0 = new FileReader();
        INSTANCE = var0;
    }
}
