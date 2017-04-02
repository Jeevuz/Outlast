package me.jeevuz.outlast.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import me.jeevuz.outlast.R;

/**
 * @author Vasili Chyrvon (vasili@mobileup.ru)
 */
public class MainFragment extends BaseFragment<MainPresentationModel> {

    private EditText input;
    private TextView output;
    private Button childrenButton;

    @Override
    protected MainPresentationModel providePresentationModel() {
        return new MainPresentationModel();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input = (EditText) view.findViewById(R.id.input);
        output = (TextView) view.findViewById(R.id.output);
        childrenButton = (Button) view.findViewById(R.id.childrenButton);
    }

    @Override
    protected void bindPresentationModel(MainPresentationModel presentationModel) {
        // State

        // Input text
        bindState(presentationModel.inputText(), string -> {
                    input.setText(string);
                    input.setSelection(input.length());
                }
        );

        // Output text
        bindState(presentationModel.outputText(), output::setText);

        // Children presence
        bindState(presentationModel.childrenPresence(), isPresented -> {
                    changeChildrenPresence(isPresented);
                    childrenButton.setText(isPresented ? R.string.children_button_hide
                            : R.string.children_button_show);
                }
        );

        // Actions

        // Tell presentation model that input text changed
        bindAction(
                RxTextView.textChanges(input)
                        .map(CharSequence::toString),
                presentationModel.inputTextChanges
        );

        // Tell presentation model that children button was clicked
        bindAction(
                RxView.clicks(childrenButton),
                presentationModel.childrenButtonClicks
        );
    }

    private void changeChildrenPresence(boolean isPresented) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();

        Fragment child1 = childFragmentManager.findFragmentById(R.id.childContainer1);
        Fragment child2 = childFragmentManager.findFragmentById(R.id.childContainer2);

        if (isPresented) {
            // Add children if they aren't presented
            if (child1 == null) {
                fragmentTransaction.add(R.id.childContainer1, new ChildFragment());
            }
            if (child2 == null) {
                fragmentTransaction.add(R.id.childContainer2, new ChildFragment());
            }
        } else {
            // Remove children if they are presented
            if (child1 != null) {
                fragmentTransaction.remove(child1);
            }
            if (child2 != null) {
                fragmentTransaction.remove(child2);
            }
        }

        fragmentTransaction.commit();
    }
}
