package me.jeevuz.outlast.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import me.jeevuz.outlast.R;

/**
 * @author Vasili Chyrvon (vasili.chyrvon@gmail.com)
 */
public class ChildFragment extends BaseFragment<ChildPresentationModel> {

    private static int intervalSpeedSeconds = 1;
    private TextView swearText;

    @Override
    protected ChildPresentationModel providePresentationModel() {
        return new ChildPresentationModel(getIntervalSpeedSeconds());
    }

    private int getIntervalSpeedSeconds() {
        if (intervalSpeedSeconds > 2) {
            intervalSpeedSeconds = 1;
        }
        return intervalSpeedSeconds++;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_child;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swearText = (TextView) view.findViewById(R.id.swear);
    }

    @Override
    protected void bindPresentationModel(ChildPresentationModel presentationModel) {
        // Interval text
        bindState(presentationModel.intervalText(), swearText::setText);
    }
}
