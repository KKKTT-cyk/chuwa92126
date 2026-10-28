import java.util.Map;

public final class ExprsTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        Expr expression = new Add(new Num(3), new Mul(new Var("x"), new Num(2)));
        check(Exprs.eval(expression, Map.of("x", 4.0)) == 11.0, "eval");
        check(Exprs.show(expression).equals("(3 + (x * 2))"), "show");
        check(Exprs.show(new Num(3.5)).equals("3.5"), "fractional show");
        check(Exprs.simplify(new Add(new Num(2), new Num(3))).equals(new Num(5)), "fold add");
        check(Exprs.simplify(new Mul(new Num(0), new Var("x"))).equals(new Num(0)), "zero multiply");
        check(Exprs.simplify(new Add(new Num(0), new Var("x"))).equals(new Var("x")), "zero add");
        check(Exprs.simplify(new Neg(new Neg(new Var("x")))).equals(new Var("x")), "double negation");
        check(Exprs.simplify(new Neg(new Num(4))).equals(new Num(-4)), "constant negation");
        try {
            Exprs.eval(new Var("missing"), Map.of());
            throw new AssertionError("expected unbound variable exception");
        } catch (IllegalArgumentException expected) {
            check(expected.getMessage().equals("Unbound variable: missing"), "exception message");
        }
        System.out.println("All tests passed.");
    }
}
