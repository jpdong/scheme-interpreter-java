package com.dong.interpreter.func;

import com.dong.interpreter.Type;
import com.dong.interpreter.data.Node;

import java.util.List;

public class CdrProc extends Func<Type> {
    @Override
    public Type accept(List arguments) {
        Node node = (Node) arguments.get(0);
        if (node.typeList.size() < 3) {
            return node.typeList.get(1);
        } else {
            Node result = new Node();
            for (int i = 1; i < node.typeList.size(); i++) {
                result.typeList.add(node.typeList.get(i));
            }
            return result;
        }

    }
}
