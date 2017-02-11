package me.jeevuz.outlast;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton that stores the map of Outlastings.
 * Store is needed to allow them to outlast the orientation changes.
 *
 * @author Vasili Chyrvon (vasili.chyrvon@gmail.com)
 */
final class Store {

    static final Store INSTANCE = new Store();

    private Store() {
    }

    private Map<String, Outlasting> outlastingsMap = new HashMap<>();


    /**
     * Returns stored or newly created Outlasting
     *
     * @param creator creator to instantiate the Outlasting if the map doesn't contain it.
     * @param tag     store tag. The same outlasting will be returned for the same tag,
     *                so tag must be unique for different callers.
     */
    <T extends Outlasting> T getOutlasting(@NonNull Outlasting.Creator<T> creator, @NonNull String tag) {

        if (!outlastingsMap.containsKey(tag)) {
            T outlasting = creator.createOutlasting();
            outlasting.onCreate();
            outlastingsMap.put(tag, outlasting);
        }

        //noinspection unchecked
        return (T) outlastingsMap.get(tag);
    }

    /**
     * Removes the Outlasting for the passed tag from the store, allowing it to be destroyed.
     *
     * @param tag store tag to remove the Outlasting.
     */
    void removeOutlasting(@NonNull String tag) {
        Outlasting outlasting = outlastingsMap.get(tag);

        if (outlasting != null) {
            outlasting.onDestroy();
            outlastingsMap.remove(tag);
        }
    }
}
