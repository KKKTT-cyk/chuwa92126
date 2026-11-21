import java.util.HashMap;
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
        return switch (expr){
            case Num(double value) -> value;
            case Var(String name) -> {
                Double value = env.get(name);
                if (value==null){
                    throw new IllegalArgumentException(
                            "Unbound variable: "+name
                    );
                }
                yield value;
            }
            case Add(Expr left, Expr right) ->
                    eval(left,env)+eval(right,env);
            case Mul(Expr left, Expr right) ->
                    eval(left,env)*eval(right,env);
            case Neg(Expr operand) ->
                0.0 - eval(operand,env);


        };
    }
    public static String show(Expr expr) {
        return switch (expr){
            case Num(double value) ->{
                if (Double.isFinite(value) && value==Math.rint(value)){
                    yield java.math.BigDecimal.valueOf(value)
                            .stripTrailingZeros()
                            .toPlainString();
                }
                yield Double.toString(value);
            }
            case Var(String name) -> name;
            case Add(Expr left, Expr right) ->
                "(" +show(left)+" + "+show(right)+")";
            case Mul(Expr left, Expr right) ->
                "("+show(left)+" * "+show(right)+")";
            case Neg(Expr operand) ->
                "-"+show(operand);

        };
    }
    public static Expr simplify(Expr expr) {
        return switch (expr){
            case Num(double value) -> expr;
            case Var(String name) -> expr;
            case Add(Expr left, Expr right) ->{
                Expr l =simplify(left);
                Expr r = simplify(right);

                if (l instanceof Num(double a) && r instanceof Num(double b)){
                    yield new Num(a+b);
                }
                if (l instanceof Num(double a) && a==0.0){
                    yield r;
                }
                if (r instanceof Num(double b) && b==0.0){
                    yield l;
                }
                yield new Add(l,r);
            }
            case Mul(Expr left, Expr right) ->{
                Expr l = simplify(left);
                Expr r = simplify(right);

                if(l instanceof Num(double a) && r instanceof Num(double b)){
                    yield new Num(a*b);
                }
                if (l instanceof Num(double a) && a==1.0){
                    yield r;
                }
                if (r instanceof Num(double b) && b==1.0){
                    yield l;
                }
                if (l instanceof Num(double a) && a==0.0){
                    yield new Num(0.0);
                }
                if (r instanceof Num(double b) && b==0.0){
                    yield new Num(0.0);
                }

                yield new Mul(l,r);
            }
            case Neg(Expr operand) ->{
                Expr simplified = simplify(operand);
                if(simplified instanceof Num(double n)){
                    yield new Num(0.0-n);
                }
                if (simplified instanceof Neg(Expr inner)){
                    yield inner;
                }
                yield new Neg(simplified);
            }
        };
    }
    
}

