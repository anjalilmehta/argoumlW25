package org.argouml.ui.cmd;

import javax.swing.JMenuItem;
import org.argouml.ui.cmd.ShortcutMgr;
import org.argouml.uml.ui.*;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.cmd.GenericArgoMenuBar;

public class MenuBarHelper {

    public static void buildCreateMenu(GenericArgoMenuBar menuBar) {
        menuBar.getCreateDiagramMenu().removeAll();
        menuBar.getCreateDiagramToolbar().removeAll();

        addMenuItem(menuBar, new ActionUseCaseDiagram(), "Usecase Diagram", ShortcutMgr.ACTION_USE_CASE_DIAGRAM);
        addMenuItem(menuBar, new ActionClassDiagram(), "Class Diagram", ShortcutMgr.ACTION_CLASS_DIAGRAM);
        addMenuItem(menuBar, new ActionSequenceDiagram(), "Sequence Diagram", ShortcutMgr.ACTION_SEQUENCE_DIAGRAM);
        addMenuItem(menuBar, new ActionCollaborationDiagram(), "Collaboration Diagram", ShortcutMgr.ACTION_COLLABORATION_DIAGRAM);
        addMenuItem(menuBar, new ActionStateDiagram(), "State Diagram", ShortcutMgr.ACTION_STATE_DIAGRAM);
        addMenuItem(menuBar, new ActionActivityDiagram(), "Activity Diagram", ShortcutMgr.ACTION_ACTIVITY_DIAGRAM);
        addMenuItem(menuBar, new ActionDeploymentDiagram(), "Deployment Diagram", ShortcutMgr.ACTION_DEPLOYMENT_DIAGRAM);

        menuBar.getCreateDiagramToolbar().setFloatable(true);
    }

    private static void addMenuItem(GenericArgoMenuBar menuBar, UndoableAction action, String name, String shortcut) {
        JMenuItem item = menuBar.getCreateDiagramMenu().add(action);
        menuBar.setMnemonic(item, name);
        menuBar.getCreateDiagramToolbar().add(action);
        ShortcutMgr.assignAccelerator(item, shortcut);
    }
}
