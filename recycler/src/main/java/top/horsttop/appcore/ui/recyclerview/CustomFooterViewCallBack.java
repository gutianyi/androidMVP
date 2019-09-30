package top.horsttop.appcore.ui.recyclerview;

import android.view.View;

/**
 * Created by horsttop on 18/05/19.
 */

public interface CustomFooterViewCallBack {

    void onLoadingMore(View yourFooterView);
    void onLoadMoreComplete(View yourFooterView);
    void onSetNoMore(View yourFooterView,boolean noMore);

}
