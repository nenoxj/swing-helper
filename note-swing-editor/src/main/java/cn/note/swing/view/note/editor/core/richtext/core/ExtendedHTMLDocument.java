/*
GNU Lesser General Public License

PropertiesDialog
Copyright (C) 2003 Frits Jalvingh, Jerry Pommer & Howard Kistler

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

import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import javax.swing.undo.UndoableEdit;
import java.util.Enumeration;

public class ExtendedHTMLDocument extends HTMLDocument {

    public ExtendedHTMLDocument(AbstractDocument.Content c, StyleSheet styles) {
        super(c, styles);
    }

    public ExtendedHTMLDocument(StyleSheet styles) {
        super(styles);
    }

    public ExtendedHTMLDocument() {

    }

    /**
     * �berschreibt die Attribute des Elements.
     *
     * @param e   Element bei dem die Attribute ge�ndert werden sollen
     * @param a   AttributeSet mit den neuen Attributen
     * @param tag Angabe was f�r ein Tag das Element ist
     */
    public void replaceAttributes(Element e, AttributeSet a, HTML.Tag tag) {
        if ((e != null) && (a != null)) {
            try {
                writeLock();
                int start = e.getStartOffset();
                DefaultDocumentEvent changes = new DefaultDocumentEvent(start, e.getEndOffset() - start, DocumentEvent.EventType.CHANGE);
                AttributeSet sCopy = a.copyAttributes();
                changes.addEdit(new AttributeUndoableEdit(e, sCopy, false));
                MutableAttributeSet attr = (MutableAttributeSet) e.getAttributes();
                Enumeration aNames = attr.getAttributeNames();
                Object value;
                Object aName;
                while (aNames.hasMoreElements()) {
                    aName = aNames.nextElement();
                    value = attr.getAttribute(aName);
                    if (value != null && !value.toString().equalsIgnoreCase(tag.toString())) {
                        attr.removeAttribute(aName);
                    }
                }
                attr.addAttributes(a);
                changes.end();
                fireChangedUpdate(changes);
                fireUndoableEditUpdate(new UndoableEditEvent(this, changes));
            } finally {
                writeUnlock();
            }
        }
    }

    public void removeElements(Element e, int index, int count)
            throws BadLocationException {
        writeLock();
        int start = e.getElement(index).getStartOffset();
        int end = e.getElement(index + count - 1).getEndOffset();
        try {
            Element[] removed = new Element[count];
            Element[] added = new Element[0];
            for (int counter = 0; counter < count; counter++) {
                removed[counter] = e.getElement(counter + index);
            }
            DefaultDocumentEvent dde = new DefaultDocumentEvent(start, end - start, DocumentEvent.EventType.REMOVE);
            ((AbstractDocument.BranchElement) e).replace(index, removed.length, added);
            dde.addEdit(new ElementEdit(e, index, removed, added));
            UndoableEdit u = getContent().remove(start, end - start);
            if (u != null) {
                dde.addEdit(u);
            }
            postRemoveUpdate(dde);
            dde.end();
            fireRemoveUpdate(dde);
            if (u != null) {
                fireUndoableEditUpdate(new UndoableEditEvent(this, dde));
            }
        } finally {
            writeUnlock();
        }
    }

    /**
     * 刷新
     */
    public void refresh() {
    }

    public Element getParagraph(int offset) {
        Element paragraph = null;
        Element elem = getDefaultRootElement();

        while (!elem.isLeaf()) {
            if (elem.getName().equals("paragraph"))
                paragraph = elem;
            elem = elem.getElement(elem.getElementIndex(offset));
        }
        return paragraph;
    }

    public void deleteLastParagraph() {
        Element par = getParagraph(0);
        if (par == null) {
            return;
        }
        int start = par.getStartOffset();
        int end = par.getEndOffset();
        AbstractDocument.DefaultDocumentEvent e = new AbstractDocument.DefaultDocumentEvent(0, getLength() - 1, DocumentEvent.EventType.REMOVE);
        try {
            getContent().remove(start, end - start);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        fireRemoveUpdate(e);
    }


    public void controlUndo(int start, int end, Runnable runnable) {
        try {
            writeLock();
            DefaultDocumentEvent changes = new DefaultDocumentEvent(start, end, DocumentEvent.EventType.CHANGE);
            runnable.run();
            changes.end();
            fireChangedUpdate(changes);
            fireUndoableEditUpdate(new UndoableEditEvent(this, changes));
        } finally {
            writeUnlock();
        }
    }

}