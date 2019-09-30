package top.horsttop.appcore.load.callback;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import top.horsttop.appcore.R;


/**
 * Created by horsttop on 2016/04/10.
 */

public class TimeoutCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_timeout;
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        Toast.makeText(context.getApplicationContext(),"Connecting to the network again!",Toast.LENGTH_SHORT).show();
        return false;
    }

}
