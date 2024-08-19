package com.hns.acumen360.utils.commonmark.internal;

import com.hns.acumen360.utils.commonmark.node.Block;
import com.hns.acumen360.utils.commonmark.node.LinkReferenceDefinition;
import com.hns.acumen360.utils.commonmark.node.Paragraph;
import com.hns.acumen360.utils.commonmark.node.SourceSpan;
import com.hns.acumen360.utils.commonmark.parser.InlineParser;
import com.hns.acumen360.utils.commonmark.parser.SourceLine;
import com.hns.acumen360.utils.commonmark.parser.SourceLines;
import com.hns.acumen360.utils.commonmark.parser.block.AbstractBlockParser;
import com.hns.acumen360.utils.commonmark.parser.block.BlockContinue;
import com.hns.acumen360.utils.commonmark.parser.block.ParserState;

import java.util.List;

public class ParagraphParser extends AbstractBlockParser {

    private final Paragraph block = new Paragraph();
    private final LinkReferenceDefinitionParser linkReferenceDefinitionParser = new LinkReferenceDefinitionParser();

    @Override
    public boolean canHaveLazyContinuationLines() {
        return true;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        if (!state.isBlank()) {
            return BlockContinue.atIndex(state.getIndex());
        } else {
            return BlockContinue.none();
        }
    }

    @Override
    public void addLine(SourceLine line) {
        linkReferenceDefinitionParser.parse(line);
    }

    @Override
    public void addSourceSpan(SourceSpan sourceSpan) {
        // Some source spans might belong to link reference definitions, others to the paragraph.
        // The parser will handle that.
        linkReferenceDefinitionParser.addSourceSpan(sourceSpan);
    }

    @Override
    public void closeBlock() {
        if (linkReferenceDefinitionParser.getParagraphLines().isEmpty()) {
            block.unlink();
        } else {
            block.setSourceSpans(linkReferenceDefinitionParser.getParagraphSourceSpans());
        }
    }

    @Override
    public void parseInlines(InlineParser inlineParser) {
        SourceLines lines = linkReferenceDefinitionParser.getParagraphLines();
        if (!lines.isEmpty()) {
            inlineParser.parse(lines, block);
        }
    }

    public SourceLines getParagraphLines() {
        return linkReferenceDefinitionParser.getParagraphLines();
    }

    public List<LinkReferenceDefinition> getDefinitions() {
        return linkReferenceDefinitionParser.getDefinitions();
    }
}
