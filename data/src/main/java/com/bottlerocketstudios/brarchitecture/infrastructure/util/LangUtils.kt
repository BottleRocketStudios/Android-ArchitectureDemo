package com.bottlerocketstudios.brarchitecture.infrastructure.util

/**
 * Forces exhaustive `when` statement (all cases MUST be listed) when added like `when (foo) {}.exhaustive`.
 * Useful when you don't need the result of the when (ie using `when` as a statement instead of an expression) but still want to enforce the exhaustive requirement.
 * Prefer to list out all cases rather than using `else` to enforce compilation errors at the callsite when the conditions list changes (ex: new enum added). This
 * forces a dev to look at a specific area of code and determine how to handle it vs letting the updated conditions silently fall through to the else with no explicit indication.
 *
 * Example below:
```
// `when` is used as a statement, so the
// compiler does not check for exhaustiveness
when (number) {
 is Numbers.One -> println("This is One")
 is Numbers.Two -> println("This is Two")
}

// by calling `exhaustive`, we turn `when` from a statement
// to an expression 👍
when (number) {
 is Numbers.One -> println("This is One")
 is Numbers.Two -> println("This is Two")
 // 'when' expression must be exhaustive, add necessary 'is Three' branch or 'else' branch instead
}.exhaustive
```
 *
 * See https://medium.com/androiddevelopers/sealed-with-a-class-a906f28ab7b5
 */
@Suppress("unused")
val <T> T.exhaustive: T
    get() = this
