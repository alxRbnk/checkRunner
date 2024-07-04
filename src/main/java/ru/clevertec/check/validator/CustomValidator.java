package ru.clevertec.check.validator;

public interface CustomValidator<T> {
    void validate(T t);
}
