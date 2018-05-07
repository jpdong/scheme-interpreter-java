package com.dong.interpreter.func;

import com.dong.interpreter.data.Node;

import java.util.List;

public class ListProc extends Func<Node> {

    @Override
    public Node accept(List arguments) {
        Node node = new Node();
        node.typeList.addAll(arguments);
        return node;
    }
}
