# SchemeInterpreter

编译器前端工作一般包括两个大步骤：词法分析和语法分析，再就是解释执行或者生成中间代码，其中还有符号表（有的地方也称环境）贯穿这两个步骤，上述文章里的符号表是用单链表实现，简单易懂，方便教学，我们也可以试着用链式哈希表实现。源程序一般以字符串的形式输入，对输入字符逐个扫描分析，生成抽象语法树（`abstract syntax tree`），再根据每个树节点的符号、类型去进行语法分析，解释执行出最后结果。如果是用Scheme自举的话，词法分析几乎不需要做。

<!--more-->

#### 词法分析
如果是复杂的程序，词法分析是需要预读后面多个字符，书中也提到了一些预读缓存优化方案。我开始也尝试着使用预读判断的方案，`Java`中的`PushbackInputStream`可以手动实现预读单个字符，但对于Scheme语言这么规整的格式，我们完全可以不用预读，通过对输入字符串的整体处理，按空格分开，生成有意义的字符串数组。
>对于"(+"中没有空格的情况，参考 https://github.com/jpdong/pytudes/blob/master/py/lis.py 中的方式，可以先将"("替换成" ( "，前后分别添加空格解决。

```
private List<Type> parse(String program) {
        Queue<String> queue = tokenize(program);
        List<Type> astList = new ArrayList<>();
        while (!queue.isEmpty()) {
            astList.add(readFromTokens(queue));
        }
        return astList;
    }

    private Queue<String> tokenize(String s) {
        String string = s.replace("(", " ( ").replace(")", " ) ");
        String[] strings = string.split("\\s+");
        Queue<String> queue = new LinkedList<>();
        for (int i = 0; i < strings.length; i++) {
            if (!"".equals(strings[i])) {
                queue.offer(strings[i]);
            }
        }
        return queue;
    }
```
#### 语法分析  
![](/images/ast.png)

对于每一个括号对，我们可以把它看做是树的一个节点。和二叉树略有不同的是，父节点的子节点的个数是不确定的，需要通过语法分析最终决定，所以在这里我们可以考虑用数组全部存下来。
```
private Type readFromTokens(Queue<String> tokens) {
        if (tokens.isEmpty()) {
            throw new RuntimeException("unexpected end while reading");
        } else {
            String s = tokens.poll();
            if (s.equals("(")) {
                Node node = new Node();
                while (!")".equals(tokens.peek())) {
                    node.list.add(readFromTokens(tokens));
                }
                tokens.poll();
                return node;
            } else if (")".equals(s)) {
                throw new RuntimeException("unexpected ) while reading");
            } else {
                return atom(s);
            }
        }
    }
```
括号对里的单个元素当作基础类型处理，目前涉及到布尔型（`Bool`）、整数型（`Int`）、字符型（`Char`）、字符串型（`Str`）,这里我们把函数也看做类型，函数型（`Func`）。
```
private Type atom(String s) {
        if (isMatchChar(s)) {
            return new Char(s.charAt(2));
        } else if (isMatchString(s)) {
            return new Str(s.substring(1,s.length()-1));
        } else if (isInt(s)) {
            return new Int(Integer.valueOf(s));
        } else {
            return new Symbol(s);
        }
    }
```
语法分析就是将代码字符串处理成数据结构抽象语法树，只涉及到常用的字符串处理和一些简单的正则匹配，接下来就是要对语法树进行语言特性上的实现。

#### 符号表
在语言特性分析之前，先说一下符号表。
>在计算机科学中，符号表是一种用于语言翻译器（例如编译器和解释器）中的数据结构。在符号表中，程序源代码中的每个标识符都和它的声明或使用信息绑定在一起，比如其数据类型、作用域以及内存地址。

可以简单理解为对当前代码环境的实现，多用于查找变量。最简单的可以用单链表实现，但链表查找效率很低，链式哈希表是个很好的解决方案。
![](/images/scheme_env.png)
如图所示，每进入一个函数域，或者是每次声明局部变量，都会新建一个符号表，作为当前的计算环境，并存储上一个环境的地址。在需要查找符号表的时候，先在当前环境查找，找不到再到前一个环境中查找。每个域都只保存自己的环境地址，所以不会影响到其它域的使用。

#### 解释执行
由于没有做尾递归优化，很容易就栈溢出了，在这里能用循环就尽量用循环。
分别对`Scheme`中的操作符作处理。

- `quote`
```
if ("quote".equals(symbol.id)) {
                return list.get(1);
            }
```

- `if`
```
if ("if".equals(symbol.id)) {
                Node test = (Node) list.get(1);
                Type trueBody = list.get(2);
                Type falseBody = list.get(3);
                Bool result = (Bool) eval(test, env);
                if (result.value) {
                    ast = trueBody;
                    continue;
                } else {
                    ast = falseBody;
                    continue;
                }
            }
```
- `let`
```
if ("let".equals(symbol.id)) {
                Node field = (Node) list.get(1);
                Type body = list.get(2);
                EnvFrame extEnv = new EnvFrame(env);
                for (int i = 0; i < field.typeList.size(); i++) {
                    Node pair = (Node) field.typeList.get(i);
                    Symbol s = (Symbol) pair.typeList.get(0);
                    Type result = eval(pair.typeList.get(1), extEnv);
                    extEnv.put(s.id, result);
                }
                ast = body;
                env = extEnv;
                continue;
            }
```
- `define`
```
if ("define".equals(symbol.id)) {
                Symbol s = (Symbol) list.get(1);
                Type type = list.get(2);
                env.frameMap.put(s.id, eval(type, env));
                break;
            }
```
- `lambda`
```
if ("lambda".equals(symbol.id)) {
                Node parms = (Node) list.get(1);
                Node body = (Node) list.get(2);
                return new Procedure(parms, body, env, this);
            }
```
- `cond`
```
if ("cond".equals(symbol.id)) {
                Node elseCase = (Node) list.get(list.size() - 1);
                Symbol elseTag = (Symbol) elseCase.typeList.get(0);
                if (!"else".equals(elseTag.id)) {
                    throw new RuntimeException("wrong cond case");
                }
                Type tempAst = elseCase.typeList.get(1);
                for (int i = 1; i < list.size() - 1; i++) {
                    Node currentCase = (Node) list.get(i);
                    Type predicate = currentCase.typeList.get(0);
                    if (((Bool) eval(predicate, env)).value) {
                        tempAst = currentCase.typeList.get(1);
                        break;
                    }
                }
                ast = tempAst;
                continue;
            }
```
