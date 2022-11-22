package cn.note.swing.core.view.tree;

import javax.annotation.Nonnull;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * @description: 拖拽传输对象
 * from Java Swing Hacks
 * @author: jee
 */
public class TreeNodeTransferable implements Transferable {
    public static final String NAME = "TREE-TEST";
    private static final DataFlavor FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, NAME);
    // private static final DataFlavor[] supportedFlavors = {FLAVOR};
    private final Object object;

    protected TreeNodeTransferable(Object o) {
        object = o;
    }

    @Override
    @Nonnull
    public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException {
        if (isDataFlavorSupported(df)) {
            return object;
        } else {
            throw new UnsupportedFlavorException(df);
        }
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor df) {
        return NAME.equals(df.getHumanPresentableName());
        // return (df.equals(FLAVOR));
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{FLAVOR};
    }
}