package nl.trifork.hippo.common.workflowevents;

import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.standardworkflow.WorkflowEventWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import java.rmi.RemoteException;

/**
 * Workflow event listener that registers the documents that have been unpublished.
 *
 * @author Jettro Coenradie
 */
@PersistenceCapable
public class SearchIndexUpdateUnpublishedDocumentEvent extends AbstractWorkflowEvent implements WorkflowEventWorkflow {
    private static final Logger logger = LoggerFactory.getLogger(SearchIndexUpdateUnpublishedDocumentEvent.class);

    private static final long serialVersionUID = 1L;

    @Persistent(column = "jcr:uuid")
    private String jcrUuid;

    private SearchIndexUpdateHelper helper;

    public SearchIndexUpdateUnpublishedDocumentEvent() throws RemoteException {
        super();
        helper = new SearchIndexUpdateHelper(this);
    }

    @Override
    public void fire() throws WorkflowException, RepositoryException, RemoteException {
        logger.debug("{} fired: jcrUuid={}", this.getClass().getName(), jcrUuid);
        try {
            Long scheduledDate = helper.getScheduledDate(getDocumentNode());
            long currentTime = System.currentTimeMillis();
            if (scheduledDate <= currentTime) {
                helper.unpublishedDocument();
            }
        } catch (RepositoryException e) {
            logger.error("RepositoryException by uuid {} on firing event", jcrUuid, e);
        }

    }

    @Override
    protected String getJcrUuid() {
        return jcrUuid;
    }
}
