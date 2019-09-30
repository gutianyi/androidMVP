package top.horsttop.appcore.load.callback;

import android.content.Context;
import android.view.View;

/**
 * Created by horsttop on 2016/04/10.
 */


public class SuccessCallback extends Callback {
    public SuccessCallback(View view, Context context, OnReloadListener onReloadListener) {
        super(view, context, onReloadListener);
    }

    @Override
    protected int onCreateView() {
        return 0;
    }

    /**
     * @deprecated Use {@link #showWithCallback(boolean successVisible)} instead.
     */
    public void hide() {
        obtainRootView().setVisibility(View.INVISIBLE);
    }

    public void show() {
        obtainRootView().setVisibility(View.VISIBLE);
    }

    public void showWithCallback(boolean successVisible) {
        obtainRootView().setVisibility(successVisible ? View.VISIBLE : View.INVISIBLE);
    }

}
