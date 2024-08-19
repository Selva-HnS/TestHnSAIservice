package com.hns.acumen360.utils.commonmark.ext.gfm.tables;

import com.hns.acumen360.utils.commonmark.ext.gfm.tables.internal.TableBlockParser;
import com.hns.acumen360.utils.commonmark.ext.gfm.tables.internal.TableHtmlNodeRenderer;
import com.hns.acumen360.utils.commonmark.ext.gfm.tables.internal.TableTextContentNodeRenderer;

import com.hns.acumen360.utils.commonmark.Extension;
import com.hns.acumen360.utils.commonmark.parser.Parser;
import com.hns.acumen360.utils.commonmark.renderer.NodeRenderer;
import com.hns.acumen360.utils.commonmark.renderer.html.HtmlNodeRendererContext;
import com.hns.acumen360.utils.commonmark.renderer.html.HtmlNodeRendererFactory;
import com.hns.acumen360.utils.commonmark.renderer.html.HtmlRenderer;
import com.hns.acumen360.utils.commonmark.renderer.text.TextContentNodeRendererContext;
import com.hns.acumen360.utils.commonmark.renderer.text.TextContentNodeRendererFactory;
import com.hns.acumen360.utils.commonmark.renderer.text.TextContentRenderer;

/**
 * Extension for GFM tables using "|" pipes (GitHub Flavored Markdown).
 * <p>
 * Create it with {@link #create()} and then configure it on the builders
 * ({@link Parser.Builder#extensions(Iterable)},
 * {@link HtmlRenderer.Builder#extensions(Iterable)}).
 * </p>
 * <p>
 * The parsed tables are turned into {@link TableBlock} blocks.
 * </p>
 *
 * @see <a href="https://github.github.com/gfm/#tables-extension-">Tables (extension) in GitHub Flavored Markdown Spec</a>
 */
public class TablesExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension,
        TextContentRenderer.TextContentRendererExtension {

    private TablesExtension() {
    }

    public static Extension create() {
        return new TablesExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new TableBlockParser.Factory());
    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder) {
        rendererBuilder.nodeRendererFactory(new HtmlNodeRendererFactory() {
            @Override
            public NodeRenderer create(HtmlNodeRendererContext context) {
                return new TableHtmlNodeRenderer(context);
            }
        });
    }

    @Override
    public void extend(TextContentRenderer.Builder rendererBuilder) {
        rendererBuilder.nodeRendererFactory(new TextContentNodeRendererFactory() {
            @Override
            public NodeRenderer create(TextContentNodeRendererContext context) {
                return new TableTextContentNodeRenderer(context);
            }
        });
    }
}
