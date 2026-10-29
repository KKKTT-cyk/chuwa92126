import java.math.BigDecimal;
import java.util.Map;

public class Question4 {

    sealed interface Expr permits Num, Var, Add, Mul, Neg {
    }

    record Num(double value) implements Expr {
    }

    record Var(String name) implements Expr {
    }

    record Add(Expr left, Expr right) implements Expr {
    }

    record Mul(Expr left, Expr right) implements Expr {
    }

    record Neg(Expr operand) implements Expr {
    }

    public static final class Exprs {
        private Exprs() {
        }

        public static double eval(Expr expr, Map<String, Double> env) {
            return switch (expr) {
                case Num(double value) -> value;
                case Var(String name) -> {
                    if (!env.containsKey(name)) {
                        throw new IllegalArgumentException("Unbound variable: " + name);
                    }
                    yield env.get(name);
                }
                case Add(var left, var right) -> eval(left, env) + eval(right, env);
                case Mul(var left, var right) -> eval(left, env) * eval(right, env);
                case Neg(var operand) -> 0.0 - eval(operand, env);
            };
        }

        public static String show(Expr expr) {
            return switch (expr) {
                case Num(double value) -> {
                    if (Double.isFinite(value) && value == Math.rint(value)) {
                        // Avoid overflowing a long when displaying a large whole number.
                        yield BigDecimal.valueOf(value).toBigInteger().toString();
                    }
                    yield Double.toString(value);
                }
                case Var(String name) -> name;
                case Add(var left, var right) -> "(" + show(left) + " + " + show(right) + ")";
                case Mul(var left, var right) -> "(" + show(left) + " * " + show(right) + ")";
                case Neg(var operand) -> "-" + show(operand);
            };
        }

        public static Expr simplify(Expr expr) {
            Expr simplifiedChildren = switch (expr) {
                case Num n -> n;
                case Var v -> v;
                case Add(var left, var right) -> new Add(simplify(left), simplify(right));
                case Mul(var left, var right) -> new Mul(simplify(left), simplify(right));
                case Neg(var operand) -> new Neg(simplify(operand));
            };

            return switch (simplifiedChildren) {
                case Add(Num(double a), Num(double b)) -> new Num(a + b);
                case Mul(Num(double a), Num(double b)) -> new Num(a * b);
                case Add(Num(double a), var x) when a == 0.0 -> x;
                case Add(var x, Num(double b)) when b == 0.0 -> x;
                case Mul(Num(double a), var x) when a == 1.0 -> x;
                case Mul(var x, Num(double b)) when b == 1.0 -> x;
                case Mul(Num(double a), var x) when a == 0.0 -> new Num(0.0);
                case Mul(var x, Num(double b)) when b == 0.0 -> new Num(0.0);
                case Neg(Num(double n)) -> new Num(0.0 - n);
                case Neg(Neg(var x)) -> x;
                case Num n -> n;
                case Var v -> v;
                case Add a -> a;
                case Mul m -> m;
                case Neg n -> n;
            };
        }
    }
}
