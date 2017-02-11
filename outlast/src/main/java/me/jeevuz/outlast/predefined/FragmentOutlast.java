package me.jeevuz.outlast.predefined;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import me.jeevuz.outlast.Outlast;
import me.jeevuz.outlast.Outlasting;

/**
 * Support fragment (who uses framework ones?) version of the Outlast.
 *
 * @author Vasili Chyrvon (vasili.chyrvon@gmail.com)
 */
public class FragmentOutlast<T extends Outlasting> extends Outlast<T> {

    private Fragment fragment;

    public FragmentOutlast(@NonNull Fragment fragment,
                           @NonNull Outlasting.Creator<T> creator,
                           @Nullable Bundle savedInstanceState) {
        super(creator, savedInstanceState);
        this.fragment = fragment;
    }

    @Override
    protected boolean isPrincipalFinishing(boolean wasInstanceStateSaved) {
        return fragment.getActivity().isFinishing()
                ||
                !wasInstanceStateSaved // See http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
                        && (fragment.isRemoving() || isAnyParentOfFragmentRemoving());
    }

    // See https://github.com/Arello-Mobile/Moxy/issues/24
    private boolean isAnyParentOfFragmentRemoving() {
        boolean isAnyParentRemoving = false;

        Fragment parent = fragment.getParentFragment();
        while (!isAnyParentRemoving && parent != null) {
            isAnyParentRemoving = parent.isRemoving();
            parent = parent.getParentFragment();
        }
        return isAnyParentRemoving;
    }
}
