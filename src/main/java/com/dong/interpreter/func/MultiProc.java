package com.dong.interpreter.func;

import com.dong.interpreter.Type;
import com.dong.interpreter.data.Int;

import java.util.List;

public class MultiProc implements Func<Int> {
    @Override
    public Int accept(List<Type> arguments) {
        int sum = 1;
        if (arguments instanceof List) {
            for (int i = 0; i < arguments.size(); i++) {
                if (arguments.get(i) instanceof Int) {
                    int integer = ((Int)arguments.get(i)).getValue();
                    sum *= integer;
                }
            }
        }
        return new Int(sum);
    }
}
