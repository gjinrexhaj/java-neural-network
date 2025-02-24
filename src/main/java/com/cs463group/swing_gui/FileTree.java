package com.cs463group.swing_gui;

import com.cs463group.neural_net.utils.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.io.File;

/**
 *  FileTree.java
 *  Created on 2/22/2025
 *  Displays a file tree containing contents of specified path.
 *  Instantiated by View.java using working directory path.
 */

// TODO: Support loading training data into model via selecting .txt file from FileTree view

public class FileTree extends JTree {
    public FileTree(String path) {
        super(scan(new File(path)));
    }

    private static MutableTreeNode scan(File node) {
        DefaultMutableTreeNode ret = new DefaultMutableTreeNode(node.getName());
        if (node.isDirectory()) {
            for (File child : node.listFiles()) {
                ret.add(scan(child));
            }
        }


        return ret;
    }
}