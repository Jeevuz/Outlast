package me.jeevuz.outlast.sample;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import me.jeevuz.outlast.predefined.FragmentOutlast;
import timber.log.Timber;

/**
 * Helps to handle subscriptions and binding.
 *
 * @author Vasili Chyrvon (vasili.chyrvon@gmail.com)
 */
public abstract class BaseFragment<PM extends PresentationModel> extends Fragment {

    // To let PM outlast the configuration changes
    private FragmentOutlast<PM> outlast;

    // To unsubscribe when needed
    private CompositeDisposable composite = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the outlast delegate
        outlast = new FragmentOutlast<>(this, this::providePresentationModel, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    /**
     * Provide layout to use in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    protected abstract
    @LayoutRes
    int getFragmentLayout();

    @Override
    public void onStart() {
        super.onStart();
        outlast.onStart(); // Delegated callback
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume()");
        outlast.onResume(); // Delegated callback
        bindPresentationModel(presentationModel());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outlast.onSaveInstanceState(outState); // Delegated callback
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        outlast.onDestroy(); // Delegated callback
    }

    /**
     * Bind the Presentation Model in that method.
     * Use convenience methods bindState() and bindAction().
     */
    protected abstract void bindPresentationModel(PM presentationModel);

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause()");
        composite.clear();
    }

    /**
     * Subscribes to the passed observable with observeOn main thread and adds it to the subscriptions list.
     */
    protected <T> void bindState(Observable<T> from, Consumer<T> onNext, Consumer<Throwable> onError) {
        composite.add(
                from
                        .doOnNext(t -> Timber.d("state binding called"))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                onNext,
                                onError
                        )
        );
    }

    /**
     * Subscribes to the passed observable with observeOn main thread and adds it to the subscriptions list.
     * Not as {@link #bindState(Observable, Consumer, Consumer)} it will just log on Error.
     */
    protected <T> void bindState(Observable<T> from, Consumer<T> onNext) {
        bindState(from, onNext, Timber::e);
    }

    /**
     * Subscribes the passed subject to the
     */
    protected <T> void bindAction(Observable<T> from, PublishSubject<T> to) {
        composite.add(
                from
                        .doOnNext(t -> Timber.d("bind action called"))
                        .subscribe(to::onNext)
        );
    }

    /**
     * Returns stored Presentation Model for this fragment.
     * Call it only after onCreate().
     *
     * @return Presentation Model
     */
    protected PM presentationModel() {
        return outlast.getOutlasting();
    }

    /**
     * Provide presentation model to use with this fragment.
     */
    protected abstract PM providePresentationModel();
}
