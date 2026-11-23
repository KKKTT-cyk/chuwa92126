package org.tiff.homework13.question4;

import java.math.BigDecimal;
import java.util.Map;

public final class Exprs {
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
            case Add(Expr left, Expr right) -> eval(left, env) + eval(right, env);
            case Mul(Expr left, Expr right) -> eval(left, env) * eval(right, env);
            case Neg(Expr operand) -> -eval(operand, env);
        };
    }

    public static String show(Expr expr) {
        return switch (expr) {
            case Num(double value) -> {
                if (Double.isFinite(value) && value == Math.rint(value)) {
                    yield BigDecimal.valueOf(value).toBigInteger().toString();
                }
                yield Double.toString(value);
            }
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
            case Add(Expr left, Expr right) -> {
                Expr a = simplify(left);
                Expr b = simplify(right);
                if (a instanceof Num(double x) && b instanceof Num(double y)) {
                    yield new Num(x + y);
                }
                if (a instanceof Num(double x) && x == 0.0) {
                    yield b;
                }
                if (b instanceof Num(double y) && y == 0.0) {
                    yield a;
                }
                yield new Add(a, b);
            }
            case Mul(Expr left, Expr right) -> {
                Expr a = simplify(left);
                Expr b = simplify(right);
                if (a instanceof Num(double x) && b instanceof Num(double y)) {
                    yield new Num(x * y);
                }
                if (a instanceof Num(double x) && x == 1.0) {
                    yield b;
                }
                if (b instanceof Num(double y) && y == 1.0) {
                    yield a;
                }
                if ((a instanceof Num(double x) && x == 0.0)
                        || (b instanceof Num(double y) && y == 0.0)) {
                    yield new Num(0.0);
                }
                yield new Mul(a, b);
            }
            case Neg(Expr operand) -> {
                Expr child = simplify(operand);
                if (child instanceof Num(double value)) {
                    yield new Num(0.0 - value);
                }
                if (child instanceof Neg(Expr inner)) {
                    yield inner;
                }
                yield new Neg(child);
            }
        };
    }
}
