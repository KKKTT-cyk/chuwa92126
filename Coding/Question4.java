import java.math.BigDecimal;
import java.util.Map;

sealed interface Expr permits Num, Var, Add, Mul, Neg {}

record Num(double value) implements Expr {}
record Var(String name) implements Expr {}
record Add(Expr left, Expr right) implements Expr {}
record Mul(Expr left, Expr right) implements Expr {}
record Neg(Expr operand) implements Expr {}

final class Exprs {
    private Exprs() {}

    public static double eval(Expr expr, Map<String, Double> env) {
        return switch (expr) {
            case Num(double value) -> value;
            case Var(String name) -> {
                Double value = env.get(name);
                if (value == null) {
                    throw new IllegalArgumentException(
                        "Unbound variable: " + name
                    );
                }
                yield value;
            }
            case Add(var left, var right) ->
                eval(left, env) + eval(right, env);
            case Mul(var left, var right) ->
                eval(left, env) * eval(right, env);
            case Neg(var operand) -> 0.0 - eval(operand, env);
        };
    }

    public static String show(Expr expr) {
        return switch (expr) {
            case Num(double value) ->
                Double.isFinite(value) && value == Math.rint(value)
                    ? BigDecimal.valueOf(value).toBigInteger().toString()
                    : Double.toString(value);
            case Var(String name) -> name;
            case Add(var left, var right) ->
                "(" + show(left) + " + " + show(right) + ")";
            case Mul(var left, var right) ->
                "(" + show(left) + " * " + show(right) + ")";
            case Neg(var operand) -> "-" + show(operand);
        };
    }

    public static Expr simplify(Expr expr) {
        return switch (expr) {
            case Num n -> n;
            case Var v -> v;

            case Add(var left, var right) -> {
                Expr l = simplify(left);
                Expr r = simplify(right);

                if (l instanceof Num(double a)
                        && r instanceof Num(double b)) {
                    yield new Num(a + b);
                }
                if (l instanceof Num(double a) && a == 0.0) {
                    yield r;
                }
                if (r instanceof Num(double b) && b == 0.0) {
                    yield l;
                }
                yield new Add(l, r);
            }

            case Mul(var left, var right) -> {
                Expr l = simplify(left);
                Expr r = simplify(right);

                if (l instanceof Num(double a)
                        && r instanceof Num(double b)) {
                    yield new Num(a * b);
                }
                if (l instanceof Num(double a) && a == 1.0) {
                    yield r;
                }
                if (r instanceof Num(double b) && b == 1.0) {
                    yield l;
                }
                if (l instanceof Num(double a) && a == 0.0) {
                    yield new Num(0.0);
                }
                if (r instanceof Num(double b) && b == 0.0) {
                    yield new Num(0.0);
                }
                yield new Mul(l, r);
            }

            case Neg(var operand) -> {
                Expr simplified = simplify(operand);

                if (simplified instanceof Num(double n)) {
                    yield new Num(0.0 - n);
                }
                if (simplified instanceof Neg(var inner)) {
                    yield inner;
                }
                yield new Neg(simplified);
            }
        };
    }
}



public class Question4 {
    public static void main(String[] args) {
        // (2 * x) + -3
        Expr expr = new Add(new Mul(new Num(2), new Var("x")), new Neg(new Num(3)));
        Map<String, Double> env = Map.of("x", 5.0);
        System.out.println("show:     " + Exprs.show(expr));
        System.out.println("eval x=5: " + Exprs.eval(expr, env));

        // simplify examples
        Expr[] samples = {
            new Add(new Mul(new Num(1), new Var("x")), new Num(0)),
            new Mul(new Add(new Num(2), new Num(3)), new Var("z")),
            new Mul(new Var("y"), new Num(0)),
            new Neg(new Neg(new Var("y"))),
            new Neg(new Add(new Num(4), new Num(6))),
        };
        for (Expr sample : samples) {
            System.out.println("simplify: " + Exprs.show(sample)
                + "  =>  " + Exprs.show(Exprs.simplify(sample)));
        }

        // unbound variable
        try {
            Exprs.eval(new Var("w"), env);
        } catch (IllegalArgumentException e) {
            System.out.println("error:    " + e.getMessage());
        }
    }
}
