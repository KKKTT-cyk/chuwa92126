import java.util.Map;

sealed interface Expr permits Num, Var, Add, Mul, Neg {}
record Num(double value) implements Expr {}
record Var(String name) implements Expr {}
record Add(Expr left, Expr right) implements Expr {}
record Mul(Expr left, Expr right) implements Expr {}
record Neg(Expr operand) implements Expr {}

public final class Exprs {
    private Exprs() {}

    public static double eval(Expr expr, Map<String, Double> env) {
        return switch (expr) {
            case Num(double value) -> value;
            case Var(String name) -> {
                if (!env.containsKey(name)) {
                    throw new IllegalArgumentException("Unbound variable: " + name);
                }
                yield env.get(name);
            }
            case Add(Expr left, Expr right) -> eval(left, env) + eval(right, env);
            case Mul(Expr left, Expr right) -> eval(left, env) * eval(right, env);
            case Neg(Expr operand) -> -eval(operand, env);
        };
    }

    public static String show(Expr expr) {
        return switch (expr) {
            case Num(double value) ->
                    Double.isFinite(value) && value == Math.rint(value)
                            && value >= Long.MIN_VALUE && value < 0x1.0p63
                            ? Long.toString((long) value) : Double.toString(value);
            case Var(String name) -> name;
            case Add(Expr left, Expr right) -> "(" + show(left) + " + " + show(right) + ")";
            case Mul(Expr left, Expr right) -> "(" + show(left) + " * " + show(right) + ")";
            case Neg(Expr operand) -> "-" + show(operand);
        };
    }

    public static Expr simplify(Expr expr) {
        return switch (expr) {
            case Num(double value) -> expr;
            case Var(String name) -> expr;
            case Add(Expr left, Expr right) -> simplifyAdd(simplify(left), simplify(right));
            case Mul(Expr left, Expr right) -> simplifyMul(simplify(left), simplify(right));
            case Neg(Expr operand) -> simplifyNeg(simplify(operand));
        };
    }

    private static Expr simplifyAdd(Expr left, Expr right) {
        if (left instanceof Num(double a) && right instanceof Num(double b)) return new Num(a + b);
        if (left instanceof Num(double a) && a == 0.0) return right;
        if (right instanceof Num(double b) && b == 0.0) return left;
        return new Add(left, right);
    }

    private static Expr simplifyMul(Expr left, Expr right) {
        if (left instanceof Num(double a) && right instanceof Num(double b)) return new Num(a * b);
        if (left instanceof Num(double a) && a == 1.0) return right;
        if (right instanceof Num(double b) && b == 1.0) return left;
        if (left instanceof Num(double a) && a == 0.0) return new Num(0.0);
        if (right instanceof Num(double b) && b == 0.0) return new Num(0.0);
        return new Mul(left, right);
    }

    private static Expr simplifyNeg(Expr operand) {
        if (operand instanceof Num(double n)) return new Num(0.0 - n);
        if (operand instanceof Neg(Expr inner)) return inner;
        return new Neg(operand);
    }
}
