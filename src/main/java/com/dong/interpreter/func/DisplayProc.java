package com.dong.interpreter.func;

import com.dong.interpreter.Interpreter;
import com.dong.interpreter.Type;

import java.util.List;

public class DisplayProc extends Func<Type> {

    Interpreter interpreter;

    public DisplayProc(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public Type accept(List<Type> arguments) {
        /*String s = interpreter.listStr(arguments.get(0));
        Tool.print(s + "\n");*/
        return arguments.get(0);
    }
}
