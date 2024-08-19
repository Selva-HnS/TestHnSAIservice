package com.hns.acumen360.utils.commonmark.node

import java.util.regex.Pattern

class Text : Node {
    @JvmField
    var literal: String? = null

    constructor()
    constructor(literal: String) {
        this.literal = removeCoded(literal)
    }

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toStringAttributes(): String {
        return "literal=$literal"
    }

    private fun removeCoded(text: String): String {
        val regPart1 = "【\\d+:\\d+"
        val regPart2 = "†source】"
        val p = Pattern.compile(regPart1 + regPart2)
        return p.matcher(text).replaceAll("")
    }
}
