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

    // Evaluate value
    public static double eval(Expr expr, Map<String, Double> env) {
        return switch (expr) {
            case Num(double value) -> value;

            case Var(String name) -> {
                if (!env.containsKey(name)) {
                    throw new IllegalArgumentException(
                            "Unbound variable: " + name);
                }
                yield env.get(name);
            }

            case Add(Expr left, Expr right) ->
                eval(left, env) + eval(right, env);

            case Mul(Expr left, Expr right) ->
                eval(left, env) * eval(right, env);

            case Neg(Expr operand) ->
                -eval(operand, env);
        };
    }

    // Convert to string
    public static String show(Expr expr) {
        return switch (expr) {
            case Num(double value) -> {
                String s = Double.toString(value);
                yield s.endsWith(".0")
                        ? s.substring(0, s.length() - 2)
                        : s;
            }
            case Var(String name) -> name;

            case Add(Expr left, Expr right) ->
                "(" + show(left) + " + " + show(right) + ")";

            case Mul(Expr left, Expr right) ->
                "(" + show(left) + " * " + show(right) + ")";

            case Neg(Expr operand) ->
                "-" + show(operand);
        };
    }

    // Simplify expression
    public static Expr simplify(Expr expr) {
        return switch (expr) {

            case Num(double value) -> expr;
            case Var(String name) -> expr;

            case Add(Expr left, Expr right) -> {
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

            case Mul(Expr left, Expr right) -> {
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

            case Neg(Expr operand) -> {
                Expr x = simplify(operand);

                if (x instanceof Num(double n)) {
                    yield new Num(0.0 - n);
                }

                if (x instanceof Neg(Expr inner)) {
                    yield inner;
                }

                yield new Neg(x);
            }
        };
    }

    public static void main(String[] args) {
        Expr expr = new Add(
                new Num(3),
                new Mul(new Var("x"), new Num(1)));

        Map<String, Double> env = Map.of("x", 5.0);

        System.out.println(eval(expr, env));
        System.out.println(show(expr));
        System.out.println(show(simplify(expr)));
    }
}
