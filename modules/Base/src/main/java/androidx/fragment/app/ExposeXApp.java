package androidx.fragment.app;

import android.os.Bundle;

/**
 * 用于暴露出，androidX包里面的一些lib 定义
 * Created by holmes on 2020/6/17.
 **/
public class ExposeXApp {

    public static final String FRAGMENTS_TAG = FragmentActivity.FRAGMENTS_TAG;

//    public static final String NEXT_CANDIDATE_REQUEST_INDEX_TAG = FragmentActivity.NEXT_CANDIDATE_REQUEST_INDEX_TAG;
//    public static final String ALLOCATED_REQUEST_INDICIES_TAG = FragmentActivity.ALLOCATED_REQUEST_INDICIES_TAG;
//    public static final String REQUEST_FRAGMENT_WHO_TAG = FragmentActivity.REQUEST_FRAGMENT_WHO_TAG;

    private static final String SAVED_COMPONENTS_KEY = "androidx.lifecycle.BundlableSavedStateRegistry.key";

    private ExposeXApp() {
    }

    public static void removeSaveInstance(Bundle outState) {
        if (outState == null) {
            return;
        }

        // 参考 saveStateRegistry
        Bundle savedInStateRegistry = outState.getBundle(SAVED_COMPONENTS_KEY);
        if (savedInStateRegistry != null) {
            // 删除所有fragment的状态
            savedInStateRegistry.remove(FRAGMENTS_TAG);
        }
    }

}
