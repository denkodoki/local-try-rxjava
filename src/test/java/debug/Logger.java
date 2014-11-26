package debug;

import rx.functions.Action1;

public enum Logger {
    ;
    public static <T> Action1<T> println(final String prefix) {
        return new Action1<T>() {
            @Override
            public void call(T t) {
                System.out.println(Thread.currentThread().getName() + "; " + prefix + t);
            }
        };
    }

    public static Action1<Throwable> printlnError(final String prefix) {
        return new Action1<Throwable>() {

            @Override
            public void call(final Throwable throwable) {
                System.out.println(Thread.currentThread().getName() + "; " + prefix + throwable);
            }
        };
    }
}
