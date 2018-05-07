package com.dong.interpreter.func;

import com.dong.interpreter.Type;
import com.dong.interpreter.data.Node;

import java.util.List;

public class CarProc extends Func<Type> {
    @Override
    public Type accept(List arguments) {
        Node node = (Node) arguments.get(0);
        return node.typeList.get(0);
    }
}
