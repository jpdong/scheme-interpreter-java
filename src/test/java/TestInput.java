import com.dong.interpreter.Interpreter;
import org.junit.Assert;
import org.junit.Test;

public class TestInput {

    Interpreter interpreter = new Interpreter();

    @Test
    public void TestLessThanProc() {
        interpreter.init();
        Assert.assertEquals("#t",interpreter.testInput("( < 1 2)"));
        Assert.assertEquals("#f",interpreter.testInput("( < 3 2)"));
    }

    @Test
    public void TestLessEqualProc() {
        interpreter.init();
        Assert.assertEquals("#t",interpreter.testInput("( <= 1 2)"));
        Assert.assertEquals("#t",interpreter.testInput("( <= 2 2)"));
    }

    @Test
    public void TestMoreThanProc() {
        interpreter.init();
        Assert.assertEquals("#f",interpreter.testInput("( > 1 2)"));
        Assert.assertEquals("#t",interpreter.testInput("( > 3 2)"));
    }

    @Test
    public void TestMoreEqualProc() {
        interpreter.init();
        Assert.assertEquals("#f",interpreter.testInput("( >= 1 2)"));
        Assert.assertEquals("#t",interpreter.testInput("( >= 2 2)"));
    }

    @Test
    public void TestEqualThanProc() {
        interpreter.init();
        Assert.assertEquals("#f",interpreter.testInput("( = 1 2)"));
        Assert.assertEquals("#t",interpreter.testInput("( = 2 2)"));
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
        Assert.assertEquals(null,interpreter.testInput("(define a 1)"));
        Assert.assertEquals(null,interpreter.testInput("(define b 10)"));
        Assert.assertEquals("20",interpreter.testInput("(let ((a 10)) (+ a b))"));
    }

    @Test
    public void TestList() {
        interpreter.init();
        Assert.assertEquals("(1 2 3)",interpreter.testInput("(list 1 2 3)"));
        Assert.assertEquals("((1 2) (3 4))",interpreter.testInput("(list (quote (1 2)) (quote (3 4)))"));
    }

    @Test
    public void TestCar() {
        interpreter.init();
        Assert.assertEquals("1",interpreter.testInput("(car (list 1 2 3))"));
        Assert.assertEquals("(1 2)",interpreter.testInput("(car (cons (cons 1 2) 3))"));
    }

    @Test
    public void TestCdr() {
        interpreter.init();
        Assert.assertEquals("(2 3)",interpreter.testInput("(cdr (list 1 2 3))"));
        Assert.assertEquals("3",interpreter.testInput("(cdr (cons (cons 1 2) 3))"));
        //(define f (lambda (x) (if (< x 1) x (f (- x 1)))))
    }

    @Test
    public void TestCond() {
        interpreter.init();
        Assert.assertEquals("1",interpreter.testInput("(cond ((< 3 2) 3)((< 1 2) 1)(else 0))"));
    }

    @Test
    public void TestChar() {
        interpreter.init();
        Assert.assertEquals(null,interpreter.testInput("(define a #\\a)"));
        Assert.assertEquals(null,interpreter.testInput("(define a #\\a)"));
    }
}
