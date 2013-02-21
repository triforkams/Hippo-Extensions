package nl.trifork.hippo.common.workflowevents;

import org.hippoecm.repository.api.MappingException;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.standardworkflow.WorkflowEventWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import java.rmi.RemoteException;

/**
 * Workflow listener for published documents that uses the {@see SearchIndexUpdateHelper} to register documents
 * that have been published.
 *
 * @author Jettro Coenradie
 */
@PersistenceCapable
public class SearchIndexUpdatePublishedDocumentEvent extends AbstractWorkflowEvent implements WorkflowEventWorkflow {
    private static final Logger logger = LoggerFactory.getLogger(SearchIndexUpdatePublishedDocumentEvent.class);
    private static final long serialVersionUID = 1L;

    @Persistent(column = "jcr:uuid")
    private String jcrUuid;

    private SearchIndexUpdateHelper helper;

    /**
     * {@inheritDoc}
     */
    public SearchIndexUpdatePublishedDocumentEvent() throws RemoteException {
        super();
        helper = new SearchIndexUpdateHelper(this);
    }

    @Override
    public void fire() throws WorkflowException, MappingException, RepositoryException, RemoteException {
        logger.debug("{} fired: jcrUuid={}", this.getClass().getName(), jcrUuid);
        try {
            Long scheduledDate = helper.getScheduledDate(getDocumentNode());
            long currentTime = System.currentTimeMillis();
            if (scheduledDate <= currentTime) {
                helper.publishedDocument();
            }
        } catch (RepositoryException e) {
            logger.error("RepositoryException by uuid {} on firing event", jcrUuid, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override protected String getJcrUuid() {
        return jcrUuid;
    }
}
