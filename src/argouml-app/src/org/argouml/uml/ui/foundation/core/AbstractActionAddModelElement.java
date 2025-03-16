package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.Icon;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * Abstract class to eliminate code duplication in adding UML model elements.
 */
public abstract class AbstractActionAddModelElement extends AbstractActionNewModelElement {

    private final String buttonKey;
    private final Icon icon;

    public AbstractActionAddModelElement(String buttonKey, String iconName) {
        super(buttonKey);
        this.buttonKey = buttonKey;
        putValue(Action.NAME, Translator.localize(buttonKey));
        
        if (iconName != null) {
            this.icon = ResourceLoaderWrapper.lookupIcon(iconName);
            putValue(Action.SMALL_ICON, this.icon);
        } else {
            this.icon = null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object target = TargetManager.getInstance().getModelTarget();
        Object ns = determineNamespace(target);
        
        Object newElement = createModelElement(ns);
        TargetManager.getInstance().setTarget(newElement);
        super.actionPerformed(e);
    }

    private Object determineNamespace(Object target) {
        if (Model.getFacade().isANamespace(target)) {
            return target;
        }
        if (Model.getFacade().isAParameter(target) && Model.getFacade().getBehavioralFeature(target) != null) {
            target = Model.getFacade().getBehavioralFeature(target);
        }
        if (Model.getFacade().isAFeature(target) && Model.getFacade().getOwner(target) != null) {
            target = Model.getFacade().getOwner(target);
        }
        if (Model.getFacade().isAEvent(target) || Model.getFacade().isAClassifier(target)) {
            return Model.getFacade().getNamespace(target);
        }
        if (Model.getFacade().isAAssociationEnd(target)) {
            target = Model.getFacade().getAssociation(target);
            return Model.getFacade().getNamespace(target);
        }
        return null;
    }

    protected abstract Object createModelElement(Object namespace);
}
