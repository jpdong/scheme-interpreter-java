package com.dong.interpreter.func;

import com.dong.interpreter.Tool;
import com.dong.interpreter.Type;
import com.dong.interpreter.data.EnvFrame;
import com.dong.interpreter.data.Int;

import java.util.List;

public class SubProc extends Func<Int> {

    @Override
    public Int accept(List<Type> arguments) {
        if (arguments.size() > 2) {
            throw new RuntimeException("expected two arguments but more than that");
        }
        Int first = (Int) arguments.get(0);
        Int second = (Int) arguments.get(1);
        return new Int(first.value - second.value);
    }

    @Override
    public void accept(List<Type> arguments, Result result, EnvFrame env) {
        if (arguments.size() > 2) {
            throw new RuntimeException("expected two arguments but more than that");
        }
        Int first = (Int) arguments.get(0);
        Int second = (Int) arguments.get(1);
        Tool.print(first.value + " - " + second.value + "\n");
        result.value = new Int(first.value - second.value);
    }
}
