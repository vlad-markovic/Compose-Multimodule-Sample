import java.util.stream.IntStream

open class SimpleCharSequence(private val value: CharSequence) : CharSequence {
    override val length: Int = value.length
    override fun chars(): IntStream = value.chars()
    override fun codePoints(): IntStream = value.codePoints()
    override fun isEmpty(): Boolean = value.isEmpty()
    override fun get(index: Int): Char = value[index]
    override fun equals(other: Any?): Boolean = other == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = value.toString()
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        value.subSequence(startIndex, endIndex)
}