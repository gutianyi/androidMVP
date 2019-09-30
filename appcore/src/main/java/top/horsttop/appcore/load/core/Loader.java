package top.horsttop.appcore.load.core;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import top.horsttop.appcore.load.callback.Callback;

/**
 * Created by horsttop on 2016/04/10.
 */
public class Loader {
    private static volatile Loader loadSir;
    private Builder builder;

    public static Loader getDefault() {
        if (loadSir == null) {
            synchronized (Loader.class) {
                if (loadSir == null) {
                    loadSir = new Loader();
                }
            }
        }
        return loadSir;
    }

    private Loader() {
        this.builder = new Builder();
    }

    private void setBuilder(@NonNull Builder builder) {
        this.builder = builder;
    }

    private Loader(Builder builder) {
        this.builder = builder;
    }

    public LoadService register(@NonNull Object target) {
        return register(target, null, null);
    }

    public LoadService register(Object target, Callback.OnReloadListener onReloadListener) {
        return register(target, onReloadListener, null);
    }

    public <T> LoadService register(Object target, Callback.OnReloadListener onReloadListener, Converter<T>
            convertor) {
        TargetContext targetContext = LoaderUtil.getTargetContext(target);
        return new LoadService<>(convertor, targetContext, onReloadListener, builder);
    }

    public static Builder beginBuilder() {
        return new Builder();
    }

    public static class Builder {
        private List<Callback> callbacks = new ArrayList<>();
        private Class<? extends Callback> defaultCallback;

        public Builder addCallback(@NonNull Callback callback) {
            callbacks.add(callback);
            return this;
        }

        public Builder setDefaultCallback(@NonNull Class<? extends Callback> defaultCallback) {
            this.defaultCallback = defaultCallback;
            return this;
        }

        List<Callback> getCallbacks() {
            return callbacks;
        }

        Class<? extends Callback> getDefaultCallback() {
            return defaultCallback;
        }

        public void commit() {
            getDefault().setBuilder(this);
        }

        public Loader build() {
            return new Loader(this);
        }

    }
}
