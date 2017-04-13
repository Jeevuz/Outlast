# Outlast

Outlast is a small library that helps to make your Presenter, ViewModel or PresentationModel persistent.


## Why?

In Android world *when configuration changes, your app restarts*. It's especially not convenient when you use one of the presentation patterns (MVP, MVVM, PresentationModel). Because background task can finish while your Presenter (ViewModel, PresentationModel) is recreating and you will lose the result. Also you forced to store the Presenter's state in the View layer to restore it after config changes.

That's why *persistent Presenter (ViewModel, PresentationModel)* proved itself as a good choice.


## How to add
Add the following lines to your build.gradle:
```groovy
dependencies {
    // Outlast
    compile 'me.jeevuz.outlast:outlast:1.1'
}
```

## How to use

Create your interface for Presenter (ViewModel, PresentationModel) that extends the Outlasting or just let them **implement Outlasting** right away.

```java
public interface PresentationModel extends Outlasting {}

public class MainPresentationModel implements PresentationModel {

    @Override
    public void onCreate() {
		// Called when Outlasting is created
    }

    @Override
    public void onDestroy() {
		// Called when Outlasting is about to be destroyed
    }
}
```

Then **create the Outlast** in your fragment/activity using predefined FragmentOutlast/ActivityOutlast class and **pass needed lifecycle callbacks**.

```java
public abstract class BaseFragment<PM extends PresentationModel> extends Fragment {

    // To let PM outlast the configuration changes
    private FragmentOutlast<PM> outlast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the outlast delegate providing Outlasting and instance state
        outlast = new FragmentOutlast<>(this, this::providePresentationModel, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        outlast.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        outlast.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outlast.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        outlast.onDestroy();
    }

    /**
     * Returns stored Presentation Model for this fragment.
     * Call it only after onCreate().
     *
     * @return Presentation Model
     */
    protected PM presentationModel() {
		// Get the outlasting PM from storage
        return outlast.getOutlasting();
    }

    /**
     * Provide presentation model to use with this fragment.
     */
    protected abstract PM providePresentationModel();
}
```

> Outlasting will be **created when** you first call the ```outlast.getOutlasting()``` method.

**That's it.**   
Now you ready to **benefit** from outlasting Presenter (ViewModel, PresentationModel).

>If you use View or something else different from Fragment or Activity, create your own implementation of Outlast.
It's easy, you only need to define when you will remove the Outlasting from the store.


## Sample
Sample is based on the RxPM pattern that uses the Outlast to let PresentationModel survive orientation changes. RxPM is RxJava based version of the [Presentation Model](http://martinfowler.com/eaaDev/PresentationModel.html) pattern.

In the app you enter some bad words into input and it tries to censor them. Also there is two child fragments to show that they can be used and that the same Views (child Fragments in this case) has different outlasting PresentationModels.

## Thanks
+ Thank to Dmitriy Gorbunov (@dmdevgo) for help with some decisions

## License

```
MIT License

Copyright (c) 2017 Vasili Chyrvon (vasili.chyrvon@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 ```
