
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    Map<String, Double> env = Map.of("x", 2.0);

    Expr expr = new Mul(
        new Add(new Num(2), new Num(3)),
        new Add(new Var("x"), new Num(0))
    );

    System.out.println("Original: " + Exprs.show(expr));
    System.out.println("Evaluated: " + Exprs.eval(expr, env));

    Expr simplified = Exprs.simplify(expr);

    System.out.println("Simplified: " + Exprs.show(simplified));
    System.out.println("Evaluated again: "
        + Exprs.eval(simplified, env));

    // Double negation
    Expr negExpr = new Neg(new Neg(new Var("x")));
    System.out.println(
        "Double negation: "
            + Exprs.show(Exprs.simplify(negExpr))
    );

    // Multiplication by zero
    Expr zeroExpr = new Mul(new Num(0), new Var("x"));
    System.out.println(
        "Multiplication by zero: "
            + Exprs.show(Exprs.simplify(zeroExpr))
    );

    // Missing variable
    try {
      Exprs.eval(new Var("y"), env);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }
}
