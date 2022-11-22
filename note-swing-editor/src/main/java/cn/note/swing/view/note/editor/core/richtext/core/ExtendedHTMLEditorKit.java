/*
GNU Lesser General Public License

ExtendedHTMLEditorKit
Copyright (C) 2001  Frits Jalvingh, Jerry Pommer & Howard Kistler

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package cn.note.swing.view.note.editor.core.richtext.core;

import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * This class extends HTMLEditorKit so that it can provide other renderer classes
 * instead of the defaults. Most important is the part which renders relative
 * image paths.
 *
 * @author <a href="mailto:jal@grimor.com">Frits Jalvingh</a>
 * @version 1.0
 */

public class ExtendedHTMLEditorKit extends HTMLEditorKit {
    /**
     * Constructor
     */
    public ExtendedHTMLEditorKit() {
    }

    /**
     * Method for returning a ViewFactory which handles the image rendering.
     */
    public ViewFactory getViewFactory() {
        return new HTMLFactoryExtended();
    }

    public Document createDefaultDocument() {
        StyleSheet styles = getStyleSheet();
        StyleSheet ss = new StyleSheet();
        ss.addStyleSheet(styles);
        ExtendedHTMLDocument doc = new ExtendedHTMLDocument(ss);
        doc.setParser(getParser());
        doc.setAsynchronousLoadPriority(4);
//        doc.setTokenThreshold(100);
        //		阻止未知的标签
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        doc.setPreservesUnknownTags(false);
        return doc;
    }

    @Override
    public void read(Reader in, Document doc, int pos) throws IOException, BadLocationException {
        //read the reader into a String
        StringBuffer buffer = new StringBuffer();
        int length;
        char[] data = new char[1024];
        while ((length = in.read(data)) != -1) {
            buffer.append(data, 0, length);
        }
        //TODO is this regex right?
        StringReader reader = new StringReader(buffer.toString().replaceAll("/>", ">"));
        super.read(reader, doc, pos);
    }

    /* Inner Classes --------------------------------------------- */

    /**
     * Class that replaces the default ViewFactory and supports
     * the proper rendering of both URL-based and local images.
     */
    public static class HTMLFactoryExtended extends HTMLFactory implements ViewFactory {
        /**
         * Constructor
         */
        public HTMLFactoryExtended() {
        }

        /**
         * Method to handle IMG tags and
         * invoke the image loader.
         */
        public View create(Element elem) {
            Object obj = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
            if (obj instanceof HTML.Tag) {
                HTML.Tag tagType = (HTML.Tag) obj;
                if (tagType == HTML.Tag.IMG) {
                    return new RelativeImageView(elem);
                }
            }
            return super.create(elem);
        }
    }
}
