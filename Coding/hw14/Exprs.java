import java.util.Map;
import java.math.BigDecimal;

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

  // 1. Evaluate an expression
  public static double eval(Expr expr, Map<String, Double> env) {
    // TODO
    return switch (expr) {
      case Num(double value) -> value;

      case Var(String name) -> {
        Double value = env.get(name);
        if (value == null) {
          throw new IllegalArgumentException(
              "Unbound variable " + name
          );
        }
        yield value;
      }

      case Add(var left, var right) -> eval(left, env) + eval(right, env);

      case Mul(var left, var right) -> eval(left, env) * eval(right, env);

      case Neg(var operand) -> -eval(operand, env);

    };
    // throw new UnsupportedOperationException("TODO");
  }

  // 2. Convernt an expression to a string
  public static String show(Expr expr) {
    // TODO
    return switch (expr) {
      case Num(double value) -> formatNumber(value);

      case Var(String name) -> name;

      case Add(var left, var right) -> "(" + show(left) + " + " + show(right) + ")";

      case Mul(var left, var right) -> "(" + show(left) + " * " + show(right) + ")";

      case Neg(var operand) -> "-" + show(operand);
    };
    // throw new UnsupportedOperationException("TODO");
  }

  // Help method to format Num
  private static String formatNumber(double value) {
    if (Double.isFinite(value) && value == Math.rint(value)) {
      return BigDecimal.valueOf(value)
          .toBigInteger()
          .toString();
    }
    return Double.toString(value);
  }

  // 3. Recursively simplify an expression
  public static Expr simplify(Expr expr) {
    // TODO
    return switch (expr) {
      case Num(double value) -> expr;

      case Var(String name) -> expr;

      case Add(var left, var right) -> simplifyAdd(simplify(left), simplify(right));

      case Mul(var left, var right) -> simplifyMul(simplify(left), simplify(right));

      case Neg(var operand) -> simplifyNeg(simplify(operand));
    };
    // throw new UnsupportedOperationException("TODO");
  }

  private static Expr simplifyAdd(Expr left, Expr right) {
    if (left instanceof Num(double a) && right instanceof Num(double b)) {
      return new Num(a + b);
    }

    if (left instanceof Num(double a) && a == 0.0) {
      return right;
    }

    if (right instanceof Num(double b) && b == 0.0) {
      return left;
    }

    return new Add(left, right);
  }

  private static Expr simplifyMul(Expr left, Expr right) {
    if (left instanceof Num(double a) && right instanceof Num(double b)) {
      return new Num(a * b);
    }

    if (left instanceof Num(double a) && a == 1.0) {
      return right;
    }

    if (right instanceof Num(double b) && b == 1.0) {
      return left;
    }

    if (left instanceof Num(double a) && a == 0.0) {
      return new Num(0.0);
    }

    if (right instanceof Num(double b) && b == 0.0) {
      return new Num(0.0);
    }

    return new Mul(left, right);
  }

  private static Expr simplifyNeg(Expr operand) {
    if (operand instanceof Num(double n)) {
      return new Num(0.0 - n);
    }

    if (operand instanceof Neg(var inner)) {
      return inner;
    }

    return new Neg(operand);
  }
}