package nl.trifork.hippo.common.workflowevents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Arrays;
import java.util.List;

/**
 * Helper for storing the uuids of documents that have been published or unpublished that are also indexed for searching.
 * <p/>
 * This class contains a list with content types that are index for the search. Other type of documents are not handled.
 * <p/>
 * The provided workflow is used to obtain a jcr session and the document node to interact with. In case of a publish
 * action we store the uuid of the document that is published. In case of an unpublish we store the uuid of the handle of
 * the node that is unpublished. We use the handle to make it easier to find the actual document in the search index that
 * uses the handle as a unique identifier.
 * <p/>
 * The published documents are stored in the repository at the path:    {@value #SEARCH_PUBLISHED_PATH}
 * The unpublished documents are stored in the repository at the path:  {@value #SEARCH_UNPUBLISHED_PATH}
 *
 * @author Jettro Coenradie
 */
public class SearchIndexUpdateHelper {
    private static final Logger logger = LoggerFactory.getLogger(SearchIndexUpdateHelper.class);

    // TODO add more types and maybe initialize this using the repository??
    static final List indexedTypes = Arrays.asList("hippogogreen:newsitem");

    static final String SEARCH_PUBLISHED_PATH = "/search/published";
    static final String SEARCH_UNPUBLISHED_PATH = "/search/unpublished";
    static final String PUBLISHED_DOCUMENT_PROPERTY = "publishedDocument";
    static final String SCHEDULED_DATE = "scheduledDate";


    private AbstractWorkflowEvent workflowEvent;

    public SearchIndexUpdateHelper(AbstractWorkflowEvent workflowEvent) {
        this.workflowEvent = workflowEvent;
    }

    /**
     * Handle the received event for a published document. We store the document with the uuid of the handle as a name,
     * we also add the property with the name publishedDocument containing the uuid of the document that is actually
     * published (Not the handle).
     * <p/>
     * Before adding a new node with the handle as Identifier and a property containing the uuid of the actual published
     * document we check if the node already exists under {@value #SEARCH_PUBLISHED_PATH}.
     * <p/>
     * While adding a node with a link to the updated document we also check if a delete action is available. If that is
     * the case, we remove the node for an outstanding delete action.
     *
     * @throws javax.jcr.RepositoryException Thrown when something goes wrong in the repository.
     */
    public void publishedDocument() throws RepositoryException {
        Node documentNode = getDocumentNode();
        String nodeType = documentNode.getPrimaryNodeType().getName();

        if (!indexedTypes.contains(nodeType)) {
            logger.info("We are not interested in objects of type : {}", nodeType);
            return;
        }

        String handleIdentifier = documentNode.getParent().getIdentifier();
        Long scheduled = getScheduledDate(documentNode);

        Session jcrSession = getJcrSession();
        // Search published nodes
        Node publishedNodes = jcrSession.getNode(SEARCH_PUBLISHED_PATH);
        Node publishedNode;
        if (publishedNodes.hasNode(handleIdentifier)) {
            publishedNode = publishedNodes.getNode(handleIdentifier);
        } else {
            publishedNode = publishedNodes.addNode(handleIdentifier);
        }
        publishedNode.setProperty(PUBLISHED_DOCUMENT_PROPERTY, documentNode.getIdentifier());
        publishedNode.setProperty(SCHEDULED_DATE, scheduled);

        // Remove previous unpublished nodes if available
        Node unpublishedNodes = jcrSession.getNode(SEARCH_UNPUBLISHED_PATH);
        if (unpublishedNodes.hasNode(handleIdentifier)) {
            jcrSession.removeItem(SEARCH_UNPUBLISHED_PATH + "/" + handleIdentifier);
        }

        jcrSession.save();
    }

    /**
     * Handle the received event for the unpublished document. In case there is no outstanding delete action for the
     * current handle we add a node with the name of the uuid of the handle to the list of nodes.
     * <p/>
     * We check if there is an outstanding update action for the same handle, if that is the case, we remove the node.
     *
     * @throws RepositoryException Thrown when something goes wrong in the repository.
     */
    public void unpublishedDocument() throws RepositoryException {
        Node documentNode = getDocumentNode();
        String nodeType = documentNode.getPrimaryNodeType().getName();
        if (!indexedTypes.contains(nodeType)) {
            logger.info("We are not interested in objects of type : {}", nodeType);
            return;
        }
        Session jcrSession = getJcrSession();

        String handleIdentifier = documentNode.getParent().getIdentifier();
        Long scheduled = getScheduledDate(documentNode);

        Node unpublishedNodes = jcrSession.getNode(SEARCH_UNPUBLISHED_PATH);
        if (!unpublishedNodes.hasNode(handleIdentifier)) {
            Node addNode = unpublishedNodes.addNode(handleIdentifier);
            addNode.setProperty(SCHEDULED_DATE, scheduled);
        }

        // Remove previous published nodes if available
        Node solrPublishedNodes = jcrSession.getNode(SEARCH_PUBLISHED_PATH);
        if (solrPublishedNodes.hasNode(handleIdentifier)) {
            jcrSession.removeItem(SEARCH_PUBLISHED_PATH + "/" + handleIdentifier);
        }

        jcrSession.save();
    }



    /* Utility methods */

    /**
     * If scheduled return that date, otherwise return now.
     *
     * @param documentNode Node representing the document to check for existence of a scheduled date
     * @return the Date represented as a long containing the milliseconds since 1970.
     * @throws RepositoryException
     */
    public Long getScheduledDate(Node documentNode) throws RepositoryException {
        Node handleNode = documentNode.getParent();
        if (handleNode.hasNode("hippo:request/hipposched:triggers/default")) {
            Node scheduled = handleNode.getNode("hippo:request/hipposched:triggers/default");
            return scheduled.getProperty("hipposched:fireTime").getDate().getTimeInMillis();
        }
        return System.currentTimeMillis();
    }

    /**
     * Convenience method that delegates to workflow
     */
    private Session getJcrSession() {
        return workflowEvent.getJcrSession();
    }

    /**
     * Convenience method that delegates to workflow
     *
     * @throws javax.jcr.RepositoryException
     */
    private Node getDocumentNode() throws RepositoryException {
        return workflowEvent.getDocumentNode();
    }

}
