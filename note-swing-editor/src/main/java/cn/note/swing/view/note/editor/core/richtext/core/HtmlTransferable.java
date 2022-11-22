package cn.note.swing.view.note.editor.core.richtext.core;

import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * 剪切板粘贴html
 */
public class HtmlTransferable implements Transferable {
    public static final DataFlavor[] supportedFlavors;
    private String htmlData;

    static {
        try {
            supportedFlavors = new DataFlavor[]{
                    new DataFlavor("text/html;class=java.lang.String"),
            };
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public HtmlTransferable(String htmlData) {
        this.htmlData = htmlData;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (DataFlavor supportedFlavor : supportedFlavors) {
            if (supportedFlavor == flavor) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return htmlData;
    }
}