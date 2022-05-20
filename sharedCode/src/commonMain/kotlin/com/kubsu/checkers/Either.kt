package com.kubsu.checkers

@Immutable
sealed class Either<out L, out R> {
    @Immutable
    data class Left<T>(val value: T) : Either<T, Nothing>()

    @Immutable
    data class Right<T>(val value: T) : Either<Nothing, T>()

    companion object
}

inline infix fun <A, B, C> Either<A, B>.map(f: (B) -> C): Either<A, C> = when (this) {
    is Either.Left -> this
    is Either.Right -> Either.Right(f(value))
}

inline infix fun <A, B, C> Either<A, B>.flatMap(f: (B) -> Either<A, C>): Either<A, C> =
    when (this) {
        is Either.Left -> this
        is Either.Right -> f(value)
    }

inline infix fun <A, B, C> Either<A, C>.mapLeft(f: (A) -> B): Either<B, C> = when (this) {
    is Either.Left -> Either.Left(f(value))
    is Either.Right -> this
}

inline fun <A, B, C> Either<A, B>.fold(
    ifLeft: (A) -> C,
    ifRight: (B) -> C
): C = when (this) {
    is Either.Left -> ifLeft(value)
    is Either.Right -> ifRight(value)
}

inline infix fun <A, B> Either<A, B>.exists(predicate: (B) -> Boolean): Boolean =
    fold({ false }, predicate)

fun <A> A.left(): Either<A, Nothing> =
    Either.Left(this)

fun <A> A.right(): Either<Nothing, A> =
    Either.Right(this)