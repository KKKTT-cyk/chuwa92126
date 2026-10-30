import java.math.BigDecimal;
import java.util.Map;

sealed interface Expr permits Num, Var, Add, Mul, Neg {}

record Num(double value) implements Expr {}
record Var(String name) implements Expr {}
record Add(Expr left, Expr right) implements Expr {}
record Mul(Expr left, Expr right) implements Expr {}
record Neg(Expr operand) implements Expr {}

public final class Exprs {

    private Exprs() {}

    // Evaluate an expression using the variable environment.
    public static double eval(
            Expr expr, Map<String, Double> env) {

        return switch (expr) {
            case Num(double value) -> value;

            case Var(String name) -> {
                Double value = env.get(name);

                if (value == null) {
                    throw new IllegalArgumentException(
                            "Unbound variable: " + name);
                }

                yield value;
            }

            case Add(Expr left, Expr right) ->
                    eval(left, env) + eval(right, env);

            case Mul(Expr left, Expr right) ->
                    eval(left, env) * eval(right, env);

            case Neg(Expr operand) ->
                    -eval(operand, env);
        };
    }

    // Convert an expression to its string representation.
    public static String show(Expr expr) {

        return switch (expr) {
            case Num(double value) -> formatNumber(value);

            case Var(String name) -> name;

            case Add(Expr left, Expr right) ->
                    "(" + show(left) + " + " + show(right) + ")";

            case Mul(Expr left, Expr right) ->
                    "(" + show(left) + " * " + show(right) + ")";

            case Neg(Expr operand) ->
                    "-" + show(operand);
        };
    }

    // Display whole numbers without decimal points.
    private static String formatNumber(double value) {

        if (Double.isFinite(value)
                && value == Math.rint(value)) {

            return BigDecimal.valueOf(value)
                    .stripTrailingZeros()
                    .toPlainString();
        }

        return Double.toString(value);
    }

    // Simplify children first, then the parent expression.
    public static Expr simplify(Expr expr) {

        return switch (expr) {
            case Num(double value) -> expr;

            case Var(String name) -> expr;

            case Add(Expr left, Expr right) ->
                    simplifyAdd(simplify(left), simplify(right));

            case Mul(Expr left, Expr right) ->
                    simplifyMul(simplify(left), simplify(right));

            case Neg(Expr operand) ->
                    simplifyNeg(simplify(operand));
        };
    }

    // Simplify addition, following the required rule order.
    private static Expr simplifyAdd(Expr left, Expr right) {

        return switch (new Add(left, right)) {

            case Add(Num(double a), Num(double b)) ->
                    new Num(a + b);

            case Add(Num(double a), Expr x)
                    when a == 0.0 -> x;

            case Add(Expr x, Num(double b))
                    when b == 0.0 -> x;

            case Add(Expr x, Expr y) ->
                    new Add(x, y);
        };
    }

    // Simplify multiplication, following the required rule order.
    private static Expr simplifyMul(Expr left, Expr right) {

        return switch (new Mul(left, right)) {

            case Mul(Num(double a), Num(double b)) ->
                    new Num(a * b);

            case Mul(Num(double a), Expr x)
                    when a == 1.0 -> x;

            case Mul(Expr x, Num(double b))
                    when b == 1.0 -> x;

            case Mul(Num(double a), Expr x)
                    when a == 0.0 -> new Num(0.0);

            case Mul(Expr x, Num(double b))
                    when b == 0.0 -> new Num(0.0);

            case Mul(Expr x, Expr y) ->
                    new Mul(x, y);
        };
    }

    // Simplify negation, following the required rule order.
    private static Expr simplifyNeg(Expr operand) {

        return switch (new Neg(operand)) {

            case Neg(Num(double n)) ->
                    new Num(0.0 - n);

            case Neg(Neg(Expr x)) -> x;

            case Neg(Expr x) -> new Neg(x);
        };
    }
}
