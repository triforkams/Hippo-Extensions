package nl.trifork.hippo.common.workflowevents;

import org.hippoecm.frontend.session.UserSession;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.ext.WorkflowImpl;
import org.hippoecm.repository.standardworkflow.WorkflowEventWorkflow;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * Base class for workflow events. The subclasses only override the fire methods that they support.
 *
 * @author Jettro Coenradie
 */
public abstract class AbstractWorkflowEvent extends WorkflowImpl implements WorkflowEventWorkflow {
    /**
     * All implementations of a work-flow must provide a single, no-argument constructor.
     *
     * @throws java.rmi.RemoteException mandatory exception that must be thrown by all Remote objects
     */
    public AbstractWorkflowEvent() throws RemoteException {
        super();
    }

    @Override
    public void fire() throws WorkflowException, RepositoryException, RemoteException {
        throw new WorkflowException("fire not supported for workflow event for node with uuid: " + getJcrUuid());
    }

    @Override
    public void fire(Document document) throws WorkflowException, RepositoryException, RemoteException {
        throw new WorkflowException("fire(Document) not supported for workflow event for node with uuid: " + getJcrUuid());
    }

    @Override
    public void fire(Iterator<Document> documentIterator) throws WorkflowException, RepositoryException, RemoteException {
        throw new WorkflowException("fire(Iterator<Document>) not supportedfor  workflow event for node with uuid: " + getJcrUuid());
    }

    /* Utility methods */
    /**
     * Ubtain the current JCR user session
     *
     * @return the JCR session
     */
    protected Session getJcrSession() {
        return UserSession.get().getJcrSession();
    }

    /**
     * Returns the document based on the identifier as provided by the method implemented by the subclass.
     *
     * @return Node representing the document we currently act upon
     * @throws RepositoryException When something goes wrong during our interaction with the repository
     */
    protected Node getDocumentNode() throws RepositoryException {
        return getJcrSession().getNodeByIdentifier(getJcrUuid());
    }

    /* Methods that need to be implemented by subclasses */

    /**
     * Utility method to obtain the uuid from the subclasses of this base class.
     *
     * @return String containing the uuid of the class
     */
    protected abstract String getJcrUuid();

}

