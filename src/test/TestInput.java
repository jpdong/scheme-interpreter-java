import com.dong.interpreter.Interpreter;
import org.junit.Assert;
import org.junit.Test;

public class TestInput {

    Interpreter interpreter = new Interpreter();

    @Test
    public void TestLessProc() {
        interpreter.init();
        Assert.assertEquals("#t",interpreter.testInput("( < 1 2)"));
        Assert.assertEquals("#f",interpreter.testInput("( < 3 2)"));
    }

    @Test
    public void TestAddProc() {
        interpreter.init();
        Assert.assertEquals("3",interpreter.testInput("( + 1 2)"));
        Assert.assertEquals("6",interpreter.testInput("( + 3 2 1)"));
    }

    @Test
    public void TestSubProc() {
        interpreter.init();
        Assert.assertEquals("-1",interpreter.testInput("( - 1 2)"));
        Assert.assertEquals("1",interpreter.testInput("( - 3 2 )"));
    }

    @Test
    public void TestMultiProc() {
        interpreter.init();
        Assert.assertEquals("2",interpreter.testInput("( * 1 2)"));
        Assert.assertEquals("6",interpreter.testInput("( * 3 2 1)"));
    }

    @Test
    public void TestDivisionProc() {
        interpreter.init();
        Assert.assertEquals("0",interpreter.testInput("( / 1 2)"));
        Assert.assertEquals("1",interpreter.testInput("( / 3 2 )"));
    }

    @Test
    public void TestIf() {
        interpreter.init();
        Assert.assertEquals("1",interpreter.testInput("( if (< 1 2) 1 0)"));
        Assert.assertEquals("1",interpreter.testInput("( if (< 3 2 ) 0 1)"));
    }

    @Test
    public void TestLet() {
        interpreter.init();
        Assert.assertEquals("2",interpreter.testInput("( let ((x 1 )) (+ x 1))"));
        Assert.assertEquals("3",interpreter.testInput("( let ((x 1 ) (y 2)) (+ x y))"));
        Assert.assertEquals("4",interpreter.testInput("( let ((x 1 ) (y (+ x 2))) (+ x y))"));
    }
}
