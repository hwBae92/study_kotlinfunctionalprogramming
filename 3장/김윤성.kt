/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */

package playground

//sealed class List<out A>
//
object Nil : List<Nothing>() {
    override fun toString(): String = "Nil"
}

//
data class Cons<out A>(val head: A, val tail: List<A>) : List<A>() {
    override fun toString(): String {
        return "{ head: $head, tail: $tail }"
    }
}

sealed class List<out A> {
    companion object {
        fun <A> of(vararg aa: A): List<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }
    }
}

// 3.1
fun <A> tail(xs: List<A>): List<A> = when (xs) {
    is Nil -> throw Exception("tail is not allowed on Nil")
    is Cons -> xs.tail
}

// 3.2
fun <A> setHead(xs: List<A>, x: A): List<A> = when (xs) {
    is Nil -> throw Exception("setHead is not allowed on Nil")
    is Cons -> Cons(x, xs)
}

// 3.3
fun <A> drop(l: List<A>, n: Int): List<A> = if (n == 0) l
else when (l) {
    is Cons -> drop(l.tail, n - 1)
    is Nil -> throw Exception("drop is not allowed on Nil")
}

// 3.4
fun <A> dropWhile(l: List<A>, block: (A) -> Boolean): List<A> = when (l) {
    is Nil -> Nil
    is Cons -> if (block(l.head)) dropWhile(l.tail, block) else l
}

fun <A> append(a1: List<A>, a2: List<A>): List<A> = when (a1) {
    is Nil -> a2
    is Cons -> Cons(a1.head, append(a1.tail, a2))
}

// 3.5
fun <A> init(l: List<A>): List<A> = when (l) {
    is Nil -> throw Exception("Nil is not allowed on init")
    is Cons ->
        if (l.tail == Nil) Nil
        else Cons(l.head, init(l.tail))
}

fun <A, B> foldRight(xs: List<A>, acc: B, block: (A, B) -> B): B = when (xs) {
    is Nil -> acc
    is Cons -> block(xs.head, foldRight(xs.tail, acc, block))
}

fun sum2(ints: List<Int>): Int = foldRight(ints, 0) { a, b -> a + b }

fun product2(doubles: List<Double>): Double = foldRight(doubles, 1.0) { a, b -> a * b }

fun <A> empty(): List<A> = Nil

// 3.8
fun <A> length(xs: List<A>): Int = foldRight(xs, acc = 0) { _, acc -> acc + 1 }

// 3.9
tailrec fun <A, B> foldLeft(xs: List<A>, acc: B, block: (B, A) -> B): B = when (xs) {
    is Nil -> acc
    is Cons -> {
        foldLeft(xs.tail, block(acc, xs.head), block)
    }
}

// 3.10
fun sum3(xs: List<Int>) = foldLeft(xs, 0) { x, y -> x + y }

fun product3(xs: List<Int>) = foldLeft(xs, 1) { x, y -> x * y }

fun length3(xs: List<Int>) = foldLeft(xs, 0) { x, _ -> x + 1 }

// 3.13
fun <A> append2(a1: List<A>, a2: List<A>): List<A> = when (a2) {
    is Nil -> Nil
    is Cons -> foldLeft(Nil, a2) { _, y -> y }
}

// 3.14
fun <A> mergeList(xs: List<List<A>>): List<A> = foldRight(
    xs,
    Nil
) { xs1: List<A>,
    xs2: List<A> ->
    foldRight(xs1, xs2) { e, l -> Cons(e, l) }
}

//3.15
fun <A> increment(xs: List<Int>): List<Int> = foldRight(xs, empty()) { i, l ->
    Cons(i + 1, l)
}

//3.16
fun toString(xs: List<Double>): List<String> = foldRight(xs, empty()) { d, l ->
    Cons(d.toString(), l)
}

//3.17
fun <A, B> map(xs: List<A>, block: (A) -> B): List<B> =
    foldRight(xs, empty()) { a: A, xs: List<B> -> Cons(block(a), xs) }

//3.18
fun <A> filter(xs: List<A>, block: (A) -> Boolean): List<A> =
    foldRight(xs, empty()) { e, l -> if (block(e)) Cons(e, l) else l }

//3.19
fun <A, B> flatMap(xa: List<A>, block: (A) -> List<B>): List<B> = foldRight(xa, empty()) { e, l -> append(block(e), l) }


//3.20
fun <A, B> filter2(xa: List<A>, block: (A) -> List<B>): List<B> = foldRight(xa, empty()) { e, l -> append(block(e), l) }

//3.21

fun addBy(aa: List<Int>, bb: List<Int>): List<Int> =
    when (aa) {
        is Nil -> empty()
        is Cons -> when (bb) {
            is Nil -> empty()
            is Cons -> Cons(aa.head + bb.head, addBy(aa.tail, bb.tail))
        }
    }

// 3.22
fun <A> zipWith(aa: List<A>, bb: List<A>, block: (A, A) -> A): List<A> =
    when (aa) {
        is Nil -> empty()
        is Cons -> when (bb) {
            is Nil -> empty()
            is Cons -> Cons(block(aa.head, bb.head), zipWith(aa.tail, bb.tail, block))
        }
    }

// 3.23
tailrec fun <A> startsWith(xs: List<A>, sub: List<A>): Boolean =
    when (xs) {
        is Nil -> sub == Nil
        is Cons -> when (sub) {
            is Nil -> true
            is Cons ->
                if (xs.head == sub.head) startsWith(xs.tail, sub.tail)
                else false
        }
    }

tailrec fun <A> hasSubsequence(xs: List<A>, sub: List<A>): Boolean =
    when (xs) {
        is Nil -> false
        is Cons -> {
            if (startsWith(xs, sub)) true
            else hasSubsequence(xs.tail, sub)
        }
    }

sealed class Tree<out T>

data class Leaf<A>(val value: A) : Tree<A>()

data class Branch<A>(val left: Tree<A>, val right: Tree<A>) : Tree<A>()

// 3.24
fun <A> size(t: Tree<A>): Int =
    when (t) {
        is Leaf -> 1
        is Branch -> 1 + size(t.left) + size(t.right)
    }

// 3.25
fun maximum(t: Tree<Int>): Int =
    when (t) {
        is Leaf -> t.value
        is Branch -> maxOf(maximum(t.left), maximum(t.right))
    }

// 3.26
fun depth(t: Tree<Int>): Int =
    when (t) {
        is Leaf -> 0
        is Branch -> maxOf(1 + depth(t.left), 1 + depth(t.right))
    }

// 3.27
fun <A, B> map(t: Tree<A>, block: (A) -> B): Tree<B> =
    when (t) {
        is Leaf -> Leaf(block(t.value))
        is Branch -> Branch(map(t.left, block), map(t.right, block))
    }

// 3.28
fun <A, B> fold(t: Tree<A>, l: (A) -> B, b: (B, B) -> B): B =
    when (t) {
        is Leaf -> l(t.value)
        is Branch -> b(fold(t.left, l, b), fold(t.right, l, b))
    }


fun main() {
//    println(tail(List.of(1, 2, 3)))
//    println(setHead((List.of(1, 2, 3)), 4))
//    println(drop((List.of(1, 2, 3)), 1))
//    println(dropWhile((List.of(1, 2, 3))) { it <= 1 })
//    println(append(List.of(1, 2, 3), List.of(4)))
//    println(init(List.of(1, 2, 3)))
//    println(sum2(List.of(1, 2, 3, 4)))
//    println(product2(List.of(1.0, 2.0, 3.0, 4.0)))
//    println(foldRight(List.of(1, 2, 3), Nil as List<Int>) { x, y -> Cons(x, y) })
//    println(length(List.of(1, 2, 3)))
//    println(foldLeft(List.of(1, 2, 3), 0) { x, y -> x + y })
//    println(sum3(List.of(1, 2, 3)))
//    println(product3(List.of(1, 2, 3)))
//    println(length3(List.of(1, 2, 3)))
//    println(append(List.of(1, 2, 3), List.of(4, 5, 6)))
//    println(mergeList(List.of(List.of(1, 2, 3), List.of(4, 5, 6))))
//    println(increment<Int>(List.of(1, 2, 3)))
//    println(toString(List.of(1.0, 2.0, 3.0)))
//    println(map(List.of(1.0, 2.0, 3.0)) { x -> x.toInt() })
//    println(filter(List.of(1, 2, 3)) { x -> x < 2 })
}
