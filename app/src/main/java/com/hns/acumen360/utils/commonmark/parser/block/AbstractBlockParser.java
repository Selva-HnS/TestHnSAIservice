package com.hns.acumen360.utils.commonmark.parser.block;

import com.hns.acumen360.utils.commonmark.node.Block;
import com.hns.acumen360.utils.commonmark.node.SourceSpan;
import com.hns.acumen360.utils.commonmark.parser.InlineParser;
import com.hns.acumen360.utils.commonmark.parser.SourceLine;

public abstract class AbstractBlockParser implements BlockParser {

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public boolean canHaveLazyContinuationLines() {
        return false;
    }

    @Override
    public boolean canContain(Block childBlock) {
        return false;
    }

    @Override
    public void addLine(SourceLine line) {
    }

    @Override
    public void addSourceSpan(SourceSpan sourceSpan) {
        getBlock().addSourceSpan(sourceSpan);
    }

    @Override
    public void closeBlock() {
    }

    @Override
    public void parseInlines(InlineParser inlineParser) {
    }

}
