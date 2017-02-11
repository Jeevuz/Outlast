package me.jeevuz.outlast.sample;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;
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

    private CompositeSubscription subscriptions = new CompositeSubscription();

    // View states
    private BehaviorSubject<String> intervalText = BehaviorSubject.create();

    public Observable<String> intervalText() {
        return intervalText.asObservable();
    }

    @Override
    public void onCreate() {
        Timber.d("Created child %s" + hashCode());

        // Interval text
        subscriptions.add(
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

        subscriptions.clear();
    }
}
