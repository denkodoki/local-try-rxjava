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
}
