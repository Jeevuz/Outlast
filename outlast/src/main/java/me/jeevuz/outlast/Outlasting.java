package me.jeevuz.outlast;

/**
 * Outlasting is something that can outlast the configuration change.
 *
 * @author Vasili Chyrvon (vasili.chyrvon@gmail.com)
 */
public interface Outlasting {

    /**
     * Outlasting creation interface.
     */
    interface Creator<T extends Outlasting> {

        /**
         * Creates the Outlasting
         */
        T createOutlasting();
    }

    /**
     * Called when this Outlasting is created
     */
    void onCreate();

    /**
     * Called before this Outlasting is about to be destroyed
     */
    void onDestroy();
}
