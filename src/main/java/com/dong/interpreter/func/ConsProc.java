package com.dong.interpreter.func;

import com.dong.interpreter.Type;
import com.dong.interpreter.data.Node;

import java.util.List;

public class ConsProc extends Func<Node> {
    @Override
    public Node accept(List<Type> arguments) {
        if (arguments.size() > 2) {
            throw new RuntimeException("expected two arguments but more than that");
        }
        Node node = new Node();
        node.typeList.addAll(arguments);
        /*for (int i = 0; i < arguments.size(); i++) {
            node.typeList.add(arguments.get(i));
        }*/
        return node;

    }
}
