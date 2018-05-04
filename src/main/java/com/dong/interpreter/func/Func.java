package com.dong.interpreter.func;

import com.dong.interpreter.Type;

import java.util.List;

public interface Func<T> extends Type {
    public T accept(List<Type> arguments);
}
