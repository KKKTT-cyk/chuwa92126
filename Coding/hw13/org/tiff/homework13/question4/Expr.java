package org.tiff.homework13.question4;

public sealed interface Expr permits Num, Var, Add, Mul, Neg {
}
