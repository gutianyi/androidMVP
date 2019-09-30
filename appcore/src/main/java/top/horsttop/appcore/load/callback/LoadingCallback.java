package top.horsttop.appcore.load.callback;

import android.content.Context;
import android.view.View;

import top.horsttop.appcore.R;


/**
 * Created by horsttop on 2016/04/10.
 */

public class LoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }

    @Override
    public boolean getSuccessVisible() {
        return super.getSuccessVisible();
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        return true;
    }
}
