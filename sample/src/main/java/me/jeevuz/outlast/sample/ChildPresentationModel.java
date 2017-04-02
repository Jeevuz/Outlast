package me.jeevuz.outlast.sample;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

import static me.jeevuz.outlast.sample.TheApplication.BAD_WORDS;

/**
 * @author Vasili Chyrvon (vasili.chyrvon@gmail.com)
 */
public final class ChildPresentationModel implements PresentationModel {

    private int intervalSpeedSeconds;

    public ChildPresentationModel(int intervalSpeedSeconds) {
        this.intervalSpeedSeconds = intervalSpeedSeconds;
    }

    private CompositeDisposable composite = new CompositeDisposable();

    // View states
    private BehaviorSubject<String> intervalText = BehaviorSubject.create();

    public Observable<String> intervalText() {
        return intervalText.hide();
    }

    @Override
    public void onCreate() {
        Timber.d("Created child %s" + hashCode());

        // Interval text
        composite.add(
                Observable.interval(intervalSpeedSeconds, TimeUnit.SECONDS)
                        .doOnNext(s -> Timber.d("Interval value: %s", s))
                        .map(intervalValue -> {
                            int position = (int)(intervalValue % BAD_WORDS.size());
                            return BAD_WORDS.get(position);
                        })
                        .subscribe(
                                string -> {
                                    intervalText.onNext((intervalText.getValue() == null) ? string
                                            : intervalText.getValue() + " " + string);
                                },
                                Timber::e)
        );
    }

    @Override
    public void onDestroy() {
        Timber.d("Destroyed child %s" + hashCode());

        composite.clear();
    }
}
