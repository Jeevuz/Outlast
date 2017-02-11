package me.jeevuz.outlast.predefined;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.jeevuz.outlast.Outlast;
import me.jeevuz.outlast.Outlasting;

/**
 * Activity version of the Outlast.
 *
 * @author Vasili Chyrvon (vasili.chyrvon@gmail.com)
 */
public class ActivityOutlast<T extends Outlasting> extends Outlast<T> {

    private Activity activity;

    public ActivityOutlast(@NonNull Activity activity,
                           @NonNull Outlasting.Creator<T> creator,
                           @Nullable Bundle savedInstanceState) {
        super(creator, savedInstanceState);
        this.activity = activity;
    }

    @Override
    protected boolean isPrincipalFinishing(boolean wasInstanceStateSaved) {
        return activity.isFinishing();
    }
}
