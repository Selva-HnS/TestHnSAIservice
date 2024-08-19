package com.hns.acumen360.utils.commonmark.node;

/**
 * A paragraph block, contains inline nodes such as {@link Text}
 */
public class Paragraph extends Block {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
