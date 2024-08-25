package dev.marinus.snapwire.validator;

public interface Validator<T> {

    boolean isValid(T t);

}
