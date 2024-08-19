package com.hns.acumen360.utils.commonmark.node;

public class BlockQuote extends Block {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
