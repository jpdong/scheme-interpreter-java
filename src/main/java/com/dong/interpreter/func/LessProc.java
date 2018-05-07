package com.dong.interpreter.func;

import com.dong.interpreter.Interpreter;
import com.dong.interpreter.Type;
import com.dong.interpreter.data.Bool;
import com.dong.interpreter.data.EnvFrame;
import com.dong.interpreter.data.Int;

import java.util.List;

public class LessProc extends Func<Bool> {

    Interpreter interpreter;

    public LessProc(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public Bool accept(List<Type> arguments) {
        if (arguments.size() > 2) {
            throw new RuntimeException("expected two arguments but more than that");
        }
        Int first = (Int) arguments.get(0);
        Int second = (Int) arguments.get(1);
        return new Bool(first.value < second.value);
    }

    @Override
    public void accept(List<Type> arguments, Result result, EnvFrame env) {
        if (arguments.size() > 2) {
            throw new RuntimeException("expected two arguments but more than that");
        }
        Int first = (Int) arguments.get(0);
        Int second = (Int) arguments.get(1);
        result.value = new Bool(first.value < second.value);
    }
}
