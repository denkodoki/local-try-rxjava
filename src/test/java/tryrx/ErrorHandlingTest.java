package tryrx;

import debug.Logger;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

public class ErrorHandlingTest {
    private static final String OOPS = "Oops!";

    @Test
    public void onExceptionResumeNextTest() {
        Observable<Object> failingStream = Observable.error(new RuntimeException(OOPS));
        Observable<Integer> fallBackStream = Observable.range(1, 3);
        failingStream
                .onExceptionResumeNext(fallBackStream)
                .doOnNext(Logger.println("message :"))
                .subscribe();
    }

    @Test
    public void onErrorReturnTest() {
        Observable<Object> failingStream = Observable.error(new RuntimeException(OOPS));
        failingStream.onErrorReturn(new Func1<Throwable, String>() {
            @Override
            public String call(Throwable throwable) {
                return throwable.toString();
            }
        })
                .doOnNext(Logger.println("returned :"))
                .subscribe();
    }

    @Test
    public void onErrorResumeNextTest() {
        Observable<Object> failingStream = Observable.error(new RuntimeException(OOPS)).startWith("A");
        Observable<String> fallBackStream = Observable.just("B", "C");
        failingStream
                .onErrorResumeNext(fallBackStream)
                .doOnNext(Logger.println("returned :"))
                .subscribe();
    }


    @Test
    public void resumeSelectorTest() {
        final Observable<?> failingStream = Observable.error(new RuntimeException(OOPS)).startWith("A");
        final Observable<?> fallBackStream = Observable.just("B", "C");
        final Observable<?> terminatingStream = Observable.error(new RuntimeException("Unexpected error!!!"));
        Func1<Throwable, Observable<?>> resumeSelector = new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                return OOPS.equals(throwable.getMessage()) ? fallBackStream : terminatingStream;
            }
        };
        Subscription subscribe = failingStream.cast(Object.class)
                .onErrorResumeNext(resumeSelector)
                .doOnNext(Logger.println("returned :"))
                .subscribe();
    }
}
