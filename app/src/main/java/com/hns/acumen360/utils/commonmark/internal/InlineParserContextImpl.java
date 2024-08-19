package com.hns.acumen360.utils.commonmark.internal;

import com.hns.acumen360.utils.commonmark.node.LinkReferenceDefinition;
import com.hns.acumen360.utils.commonmark.parser.InlineParserContext;
import com.hns.acumen360.utils.commonmark.parser.delimiter.DelimiterProcessor;

import java.util.List;

public class InlineParserContextImpl implements InlineParserContext {

    private final List<DelimiterProcessor> delimiterProcessors;
    private final LinkReferenceDefinitions linkReferenceDefinitions;

    public InlineParserContextImpl(List<DelimiterProcessor> delimiterProcessors,
                                   LinkReferenceDefinitions linkReferenceDefinitions) {
        this.delimiterProcessors = delimiterProcessors;
        this.linkReferenceDefinitions = linkReferenceDefinitions;
    }

    @Override
    public List<DelimiterProcessor> getCustomDelimiterProcessors() {
        return delimiterProcessors;
    }

    @Override
    public LinkReferenceDefinition getLinkReferenceDefinition(String label) {
        return linkReferenceDefinitions.get(label);
    }
}
