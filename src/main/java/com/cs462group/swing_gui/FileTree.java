/**
 *  FileTree.java
 *  Created on 2/22/2025
 *  Displays a file tree containing contents of specified path.
 *  Instantiated by View.java using working directory path.
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */


package com.cs462group.swing_gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.io.File;


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