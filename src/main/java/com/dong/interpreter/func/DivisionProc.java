package com.dong.interpreter.func;

import com.dong.interpreter.Type;
import com.dong.interpreter.data.Int;

import java.util.List;

public class DivisionProc implements Func<Int> {
    @Override
    public Int accept(List<Type> arguments) {
        if (arguments.size() > 2) {
            throw new RuntimeException("expected two arguments but more than that");
        }
        Int first = (Int) arguments.get(0);
        Int second = (Int) arguments.get(1);
        return new Int(first.value / second.value);
    }
}
