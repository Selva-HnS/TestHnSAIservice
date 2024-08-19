package com.hns.acumen360.utils.commonmark.internal;

import com.hns.acumen360.utils.commonmark.internal.util.Parsing;

import com.hns.acumen360.utils.commonmark.node.Block;
import com.hns.acumen360.utils.commonmark.node.BlockQuote;
import com.hns.acumen360.utils.commonmark.parser.block.AbstractBlockParser;
import com.hns.acumen360.utils.commonmark.parser.block.AbstractBlockParserFactory;
import com.hns.acumen360.utils.commonmark.parser.block.BlockContinue;
import com.hns.acumen360.utils.commonmark.parser.block.BlockStart;
import com.hns.acumen360.utils.commonmark.parser.block.MatchedBlockParser;
import com.hns.acumen360.utils.commonmark.parser.block.ParserState;

public class BlockQuoteParser extends AbstractBlockParser {

    private final BlockQuote block = new BlockQuote();

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean canContain(Block block) {
        return true;
    }

    @Override
    public BlockQuote getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        int nextNonSpace = state.getNextNonSpaceIndex();
        if (isMarker(state, nextNonSpace)) {
            int newColumn = state.getColumn() + state.getIndent() + 1;
            // optional following space or tab
            if (Parsing.isSpaceOrTab(state.getLine().getContent(), nextNonSpace + 1)) {
                newColumn++;
            }
            return BlockContinue.atColumn(newColumn);
        } else {
            return BlockContinue.none();
        }
    }

    private static boolean isMarker(ParserState state, int index) {
        CharSequence line = state.getLine().getContent();
        return state.getIndent() < Parsing.CODE_BLOCK_INDENT && index < line.length() && line.charAt(index) == '>';
    }

    public static class Factory extends AbstractBlockParserFactory {
        public BlockStart tryStart(ParserState state, MatchedBlockParser matchedBlockParser) {
            int nextNonSpace = state.getNextNonSpaceIndex();
            if (isMarker(state, nextNonSpace)) {
                int newColumn = state.getColumn() + state.getIndent() + 1;
                // optional following space or tab
                if (Parsing.isSpaceOrTab(state.getLine().getContent(), nextNonSpace + 1)) {
                    newColumn++;
                }
                return BlockStart.of(new BlockQuoteParser()).atColumn(newColumn);
            } else {
                return BlockStart.none();
            }
        }
    }
}
