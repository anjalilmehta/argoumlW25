/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.diagram;

import org.argouml.i18n.Translator;
import org.argouml.model.ActivityDiagram;
import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.activity.ActivityDiagramGraphModel;
import org.argouml.uml.diagram.activity.ui.*;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.state.ui.*;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.behavior.common_behavior.*;
import org.argouml.uml.ui.behavior.state_machines.ButtonActionNewGuard;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A diagram that has actions.<p>
 *
 */
public abstract class UMLActionDiagram extends UMLDiagram implements ActivityDiagram {

    private Action actionStartPseudoState;
    private Action actionFinalPseudoState;
    private Action actionJunctionPseudoState;
    private Action actionForkPseudoState;
    private Action actionJoinPseudoState;
    private Action actionTransition;
    private Action actionCallEvent;
    private Action actionChangeEvent;
    private Action actionSignalEvent;
    private Action actionTimeEvent;
    private Action actionGuard;
    private Action actionCallAction;
    private Action actionCreateAction;
    private Action actionDestroyAction;
    private Action actionReturnAction;
    private Action actionSendAction;
    private Action actionTerminateAction;
    private Action actionUninterpretedAction;
    private Action actionActionSequence;

    /**
     * Default constructor will become protected. All subclasses should have
     * their constructors invoke the 3-arg version of the constructor.
     *
     * @deprecated for 0.27.2 by tfmorris. Use
     *             {@link #UMLDiagram(String, Object, GraphModel)} or another
     *             explicit constructor.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public UMLActionDiagram() {
        super();
    }

    /**
     * @param ns the UML namespace of this diagram
     * @deprecated for 0.27.2 by tfmorris. Use
     *             {@link #UMLDiagram(String, Object, GraphModel)}.
     */
    @Deprecated
    public UMLActionDiagram(Object ns) {
        super(ns);
    }

    /**
     * @param name the name of the diagram
     * @param ns the UML namespace of this diagram
     * @deprecated for 0.27.2 by tfmorris. Use
     *             {@link #UMLDiagram(String, Object, GraphModel)}.
     */
    @Deprecated
    public UMLActionDiagram(String name, Object ns) {
        super(name, ns);
    }


    /**
     * Construct a new ArgoUML diagram.  This is the fully specified form
     * of the constructor typically used by subclasses.
     *
     * @param name the name of the new diagram
     * @param graphModel graph model to associate with diagram
     * @param ns the namespace which will "own" the diagram
     */
    public UMLActionDiagram(String name, Object ns, GraphModel graphModel) {
        super(name, ns, graphModel);
    }

    /**
     * Construct an unnamed diagram using the given GraphModel.
     *
     * @param graphModel graph model to associate with diagram
     */
    public UMLActionDiagram(GraphModel graphModel) {
        super(graphModel);
    }


    protected Object[] getTriggerActions() {
        Object[] actions = {
            getActionCallEvent(),
            getActionChangeEvent(),
            getActionSignalEvent(),
            getActionTimeEvent(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.activity.trigger");
        return actions;
    }

    protected Object[] getEffectActions() {
        Object[] actions = {
            getActionCallAction(),
            getActionCreateAction(),
            getActionDestroyAction(),
            getActionReturnAction(),
            getActionSendAction(),
            getActionTerminateAction(),
            getActionUninterpretedAction(),
            getActionActionSequence(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.activity.effect");
        return actions;
    }

    /**
     * @return Returns the actionFinalPseudoState.
     */
    protected Action getActionFinalPseudoState() {
        if (actionFinalPseudoState == null) {
            actionFinalPseudoState =
                new RadioAction(
                        new CmdCreateNode(
                                Model.getMetaTypes().getFinalState(),
                        	"button.new-finalstate"));
        }
        return actionFinalPseudoState;
    }
    /**
     * @return Returns the actionForkPseudoState.
     */
    protected Action getActionForkPseudoState() {
        if (actionForkPseudoState == null) {
            actionForkPseudoState =
                new RadioAction(
                        new ActionCreatePseudostate(
                                Model.getPseudostateKind().getFork(),
                        	"button.new-fork"));
        }
        return actionForkPseudoState;
    }
    /**
     * @return Returns the actionJoinPseudoState.
     */
    protected Action getActionJoinPseudoState() {
        if (actionJoinPseudoState == null) {
            actionJoinPseudoState =
                new RadioAction(
                        new ActionCreatePseudostate(
                                Model.getPseudostateKind().getJoin(),
                        	"button.new-join"));
        }
        return actionJoinPseudoState;
    }
    /**
     * @return Returns the actionJunctionPseudoState.
     */
    protected Action getActionJunctionPseudoState() {
        if (actionJunctionPseudoState == null) {
            actionJunctionPseudoState =
                new RadioAction(
                        new ActionCreatePseudostate(
                                Model.getPseudostateKind().getJunction(),
                                "button.new-junction"));
        }
        return actionJunctionPseudoState;
    }
    /**
     * @return Returns the actionStartPseudoState.
     */
    protected Action getActionStartPseudoState() {
        if (actionStartPseudoState == null) {
            actionStartPseudoState =
                new RadioAction(
                        new ActionCreatePseudostate(
                                Model.getPseudostateKind().getInitial(),
                                "button.new-initial"));
        }
        return actionStartPseudoState;
    }
   /**
     * @return Returns the actionTransition.
     */
    protected Action getActionTransition() {
        if (actionTransition == null) {
            actionTransition =
                new RadioAction(
                        new ActionSetMode(
                                ModeCreatePolyEdge.class,
                                "edgeClass",
                                Model.getMetaTypes().getTransition(),
                        "button.new-transition"));
        }
        return actionTransition;
    }

    /**
     * @return Returns the actionCallEvent.
     */
    protected Action getActionCallEvent() {
        if (actionCallEvent == null) {
            actionCallEvent = new ButtonActionNewCallEvent();
        }
        return actionCallEvent;
    }

    /**
     * @return Returns the actionCallEvent.
     */
    protected Action getActionChangeEvent() {
        if (actionChangeEvent == null) {
            actionChangeEvent = new ButtonActionNewChangeEvent();
        }
        return actionChangeEvent;
    }

    /**
     * @return Returns the actionCallEvent.
     */
    protected Action getActionSignalEvent() {
        if (actionSignalEvent == null) {
            actionSignalEvent = new ButtonActionNewSignalEvent();
        }
        return actionSignalEvent;
    }

    /**
     * @return Returns the actionCallEvent.
     */
    protected Action getActionTimeEvent() {
        if (actionTimeEvent == null) {
            actionTimeEvent = new ButtonActionNewTimeEvent();
        }
        return actionTimeEvent;
    }

    protected Action getActionGuard() {
        if (actionGuard == null) {
            actionGuard = new ButtonActionNewGuard();
        }
        return actionGuard;
    }

    protected Action getActionCallAction() {
        if (actionCallAction == null) {
            actionCallAction = ActionNewCallAction.getButtonInstance();
        }
        return actionCallAction;
    }

    protected Action getActionCreateAction() {
        if (actionCreateAction == null) {
            actionCreateAction = ActionNewCreateAction.getButtonInstance();
        }
        return actionCreateAction;
    }

    protected Action getActionDestroyAction() {
        if (actionDestroyAction == null) {
            actionDestroyAction = ActionNewDestroyAction.getButtonInstance();
        }
        return actionDestroyAction;
    }

    protected Action getActionReturnAction() {
        if (actionReturnAction == null) {
            actionReturnAction = ActionNewReturnAction.getButtonInstance();
        }
        return actionReturnAction;
    }

    protected Action getActionSendAction() {
        if (actionSendAction == null) {
            actionSendAction = ActionNewSendAction.getButtonInstance();
        }
        return actionSendAction;
    }

    protected Action getActionTerminateAction() {
        if (actionTerminateAction == null) {
            actionTerminateAction =
                ActionNewTerminateAction.getButtonInstance();
        }
        return actionTerminateAction;
    }

    protected Action getActionUninterpretedAction() {
        if (actionUninterpretedAction == null) {
            actionUninterpretedAction =
                ActionNewUninterpretedAction.getButtonInstance();
        }
        return actionUninterpretedAction;
    }

    protected Action getActionActionSequence() {
        if (actionActionSequence == null) {
            actionActionSequence =
                ActionNewActionSequence.getButtonInstance();
        }
        return actionActionSequence;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base) {
        return false;
        /* TODO: We may return the following when the
         * relocate() has been implemented.
         */
//      Model.getActivityGraphsHelper()
//      .isAddingActivityGraphAllowed(base);
    }

    @SuppressWarnings("unchecked")
    public Collection getRelocationCandidates(Object root) {
        /* TODO: We may return something useful when the
         * relocate() has been implemented. */
        Collection c =  new HashSet();
        c.add(getOwner());
        return c;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#relocate(java.lang.Object)
     */
    public boolean relocate(Object base) {
        return false;
    }
}
