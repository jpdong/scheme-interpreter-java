package com.dong.interpreter.func;

import com.dong.interpreter.Type;
import com.dong.interpreter.data.Bool;
import com.dong.interpreter.data.Int;

import java.util.List;

public class MoreEqualProc extends Func<Bool> {

    @Override
    public Bool accept(List<Type> arguments) {
        if (arguments.size() > 2) {
            throw new RuntimeException("expected two arguments but more than that");
        }
        Int first = (Int) arguments.get(0);
        Int second = (Int) arguments.get(1);
        return new Bool(first.value >= second.value);
    }
}
