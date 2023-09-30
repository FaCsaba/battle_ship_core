package com.csabapro.core.result;

/** 
 * The result type is a union between two types
 * <p>
 * T holding the correct result, while E holds the error type
 */
public class Result<T, E> {
    private T ok = null;
    private E err = null;
    private boolean isOk = false;

    private Result() {}

    public static <T, E> Result<T, E> Ok(T ok) {
        Result<T, E> r = new Result<>();
        r.ok = ok;
        r.isOk = true;
        return r;
    }
    
    public static <T, E> Result<T, E> Err(E err) {
        Result<T, E> r = new Result<>();
        r.err = err;
        r.isOk = false;
        return r;
    }

    public boolean isOk() {
        return isOk;
    }

    public boolean isErr() {
        return !isOk;
    }

    public T getOkOrDefault(T def) {
        return isOk ? ok : def;
    }

    public E getErrOrDefault(E def) {
        return isOk ? def : err;
    }

    public T unwrap() {
        if (!isOk) {
            assert false : "Unwrapped on an err item: " + err.toString();
        }
        return ok;
    }

    public E unwrapErr() {
        if (isOk) {
            assert false : "Unwrapped on an ok item: " + err.toString();
        }
        return err;
    }
}
