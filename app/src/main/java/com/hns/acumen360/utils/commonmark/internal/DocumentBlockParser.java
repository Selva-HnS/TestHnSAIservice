package com.hns.acumen360.utils.commonmark.internal;

import com.hns.acumen360.utils.commonmark.node.Block;
import com.hns.acumen360.utils.commonmark.node.Document;
import com.hns.acumen360.utils.commonmark.parser.SourceLine;
import com.hns.acumen360.utils.commonmark.parser.block.AbstractBlockParser;
import com.hns.acumen360.utils.commonmark.parser.block.BlockContinue;
import com.hns.acumen360.utils.commonmark.parser.block.ParserState;

public class DocumentBlockParser extends AbstractBlockParser {

    private final Document document = new Document();

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean canContain(Block block) {
        return true;
    }

    @Override
    public Document getBlock() {
        return document;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        return BlockContinue.atIndex(state.getIndex());
    }

    @Override
    public void addLine(SourceLine line) {
    }

}
