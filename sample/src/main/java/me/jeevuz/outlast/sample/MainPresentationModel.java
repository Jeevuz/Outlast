package me.jeevuz.outlast.sample;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static me.jeevuz.outlast.sample.TheApplication.BAD_WORDS;

/**
 * @author Vasili Chyrvon (vasili.chyrvon@gmail.com)
 */
public final class MainPresentationModel implements PresentationModel {

    // View states
    private BehaviorSubject<String> inputText = BehaviorSubject.create();
    private BehaviorSubject<String> outputText = BehaviorSubject.create();
    private BehaviorSubject<Boolean> childrenPresence = BehaviorSubject.create();

    public Observable<String> inputText() {
        return inputText.hide();
    }

    public Observable<String> outputText() {
        return outputText.hide();
    }

    public Observable<Boolean> childrenPresence() {
        return childrenPresence.hide();
    }

    // Commands from views
    public PublishSubject<String> inputTextChanges = PublishSubject.create();

    public PublishSubject<Object> childrenButtonClicks = PublishSubject.create();


    @Override
    public void onCreate() {
        Timber.d("Created main presenter");

        // Text from input
        inputTextChanges
                .doOnNext(s -> Timber.d("InputTextChanges: %s", s))
                .debounce(350, TimeUnit.MILLISECONDS) // To allow quick entering
                .doOnNext(s -> Timber.d("InputTextChanges passed through debounce"))
                .filter(s -> !s.equals(inputText.getValue())) // Filter out what was set programmatically to break the cycle
                .subscribe(
                        string -> {
                            Timber.d("Handled inputTextChanges: " + string);

                            String input = string;
                            String output = outputText.getValue();

                            for (String badWord : BAD_WORDS) {
                                if (input.contains(badWord)) {
                                    // Replace bad word with stars
                                    //noinspection ReplaceAllDot
                                    input = string.replace(badWord, badWord.replaceAll(".", "*"));

                                    // Add to bad word list if needed
                                    if (output == null) {
                                        output = badWord;
                                    } else if (!output.contains(badWord)) {
                                        output = outputText.getValue() + ", " + badWord;
                                    }
                                }
                            }
                            inputText.onNext(input);
                            if (output != null) outputText.onNext(output);

                        },
                        Timber::e);

        // First value for childs presence needed for .withLatestFrom
        childrenPresence.onNext(false);

        // Children button
        childrenButtonClicks
                .withLatestFrom(childrenPresence,
                        (aVoid, aBoolean) -> aBoolean)
                .subscribe(
                        aBoolean -> {
                            childrenPresence.onNext(!aBoolean);
                        },
                        Timber::e
                );
    }

    @Override
    public void onDestroy() {
        Timber.d("Destroyed main presenter");
    }
}
