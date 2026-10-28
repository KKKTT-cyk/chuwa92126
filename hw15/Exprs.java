import java.util.Map;

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

public final class Exprs {

    private Exprs() {
    }

    public static double eval(Expr expr, Map<String, Double> env) {
        return switch (expr) {
            case Num(double value) -> value;
            case Var(String name) -> {
                Double value = env.get(name);
                if (value == null) {
                    throw new IllegalArgumentException("Unbound variable: " + name);
                }
                yield value;
            }
            case Add(Expr left, Expr right) -> eval(left, env) + eval(right, env);
            case Mul(Expr left, Expr right) -> eval(left, env) * eval(right, env);
            case Neg(Expr operand) -> -eval(operand, env);
        };
    }

    public static String show(Expr expr) {
        return switch (expr) {
            case Num(double value) -> formatNumber(value);
            case Var(String name) -> name;
            case Add(Expr left, Expr right) -> "(" + show(left) + " + " + show(right) + ")";
            case Mul(Expr left, Expr right) -> "(" + show(left) + " * " + show(right) + ")";
            case Neg(Expr operand) -> "-" + show(operand);
        };
    }

    // 3.0 -> "3", 2.5 -> "2.5"
    private static String formatNumber(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return Double.toString(value);
    }

    public static Expr simplify(Expr expr) {
        // Step 1: simplify the children first.
        Expr e = switch (expr) {
            case Num n -> n;
            case Var v -> v;
            case Add(Expr left, Expr right) -> new Add(simplify(left), simplify(right));
            case Mul(Expr left, Expr right) -> new Mul(simplify(left), simplify(right));
            case Neg(Expr operand) -> new Neg(simplify(operand));
        };

        // Step 2: apply the rules in the order given; the first matching case wins.
        return switch (e) {
            case Add(Num(double a), Num(double b)) -> new Num(a + b);
            case Mul(Num(double a), Num(double b)) -> new Num(a * b);
            case Add(Num(double a), Expr x) when a == 0 -> x;
            case Add(Expr x, Num(double b)) when b == 0 -> x;
            case Mul(Num(double a), Expr x) when a == 1 -> x;
            case Mul(Expr x, Num(double b)) when b == 1 -> x;
            case Mul(Num(double a), Expr x) when a == 0 -> new Num(0.0);
            case Mul(Expr x, Num(double b)) when b == 0 -> new Num(0.0);
            case Neg(Num(double n)) -> new Num(0.0 - n);
            case Neg(Neg(Expr x)) -> x;
            // No rule applies: return the expression with its simplified children.
            case Num n -> n;
            case Var v -> v;
            case Add a -> a;
            case Mul m -> m;
            case Neg n -> n;
        };
    }
}