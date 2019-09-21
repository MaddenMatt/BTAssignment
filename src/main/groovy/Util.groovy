package groovy

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static groovyx.net.http.ContentTypes.JSON
import groovyx.net.http.*

class Util {
    static Logger logger = LoggerFactory.getLogger("com.util.logger")
    /***
     * Sends a get request to the specified url.
     * @param url A string that contains a http url.
     * @return A Tuple; if the response is successful the tuple contains (status code, response body) otherwise it contains (status code, error message).
     */
    def static sendGetRequest(String url) {
        //Encode certain reserved characters that might occur in an endpoint
        def encodedUrl = url.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D")
        //Configure the HttpBuilder using the encoded url and json as the content type
        def response = HttpBuilder.configure {
            request.uri = encodedUrl
            request.contentType = JSON[0]
        //Submit a get response to the url
        }.get {
            //If the response HTTP status is 200 return a tuple with the HTTP status code and the response body
            response.when(200) { FromServer fs, Object body ->
                new Tuple(fs.getStatusCode(), body)
            }
            //If the response HTTP status contains a failing status return a tuple with the HTTP status code and an error message.
            response.failure { FromServer fs, Object body ->
                new Tuple(fs.getStatusCode(), "ERROR: GET request not valid for endpoint: ${url}, returned HTTP status code was ${fs.getStatusCode()}")
            }
        }
        return response
    }

    /***
     * Retrieve a designated name for an endpoint by parsing its url.
     * @param url The url to retrieve an endpoint name for.
     * @return A string; The designated name for the endpoint.
     */
    def static getEndpointName(String url) {
        def endpointName = url.substring(url.lastIndexOf("/") + 1)
                .replaceAll("repos", "repository")
                .replaceAll("BoomTownROI", "BoomTownROI organization")
                .replaceAll("events", "event")
        return endpointName
    }

    /***
     * Writes a list of ids to the log.  Formats the output to match the endpoint the ids were retrieved from.
     * @param response The response body containing the ids.
     * @param endpointName The name of the endpoint that the ids were retrieved from.
     * @return nothing.
     */
    def static outputListIds(def response, def endpointName) {
        if (endpointName == "repository") {
            //Get the id and the name of the repository
            def ids = response.id
            def names = response.full_name
            //Iterate through the ids and log them with the corresponding name
            ids.eachWithIndex({ id, i ->
                logger.info("Repository id for ${names.get(i)}: ${id}")
            })
        } else if (endpointName == "event") {
            //Get the id and type of the event as well as the name of the repository the event occurred on
            def ids = response.id
            def types = response.type
            def repositoryNames = response.repo.name
            //Iterate through the ids and log them with the corresponding event type and repository name
            ids.eachWithIndex({ id, i ->
                logger.info("Event id for ${types.get(i)} event on ${repositoryNames.get(i)}: ${id}")
            })
        } else if (endpointName == "BoomTownROI organization") {
            //Get the id and the name of the organization
            def id = response.id
            def name = response.name
            //Log the id with the organization name
            logger.info("Organization id for ${name}: ${id}")
        }
    }
}