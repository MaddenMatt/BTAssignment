package groovy

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import groovy.Util
import java.text.SimpleDateFormat

class App {
    static void main(String[] args) {
        //Initialize the logger
        Logger logger = LoggerFactory.getLogger("com.main.logger")

        //Submit a get request to get the top-level organization details
        def boomTownRoiResponse = Util.sendGetRequest("https://api.github.com/orgs/boomtownroi")
        //Extract all of the urls in the response that contain "api.github.com/orgs/BoomTownROI"
        def urls = boomTownRoiResponse.get(1).findAll {it.value.toString().contains("api.github.com/orgs/BoomTownROI")}

        //Submit a get request to each of the urls retrieved from the top-level organization details
        urls.each {
            def response = Util.sendGetRequest(it.value.toString())
            //If the response status is 200 then log the success message, status, and list of id key/values
            if (response.get(0) == 200) {
                logger.info("SUCCESS at endpoint ${it.value}. HTTP Status ${response.get(0)}.")
                //Retrieve a designated name for the endpoint.  This helps improve the readability of the log.
                def endpointName = Util.getEndpointName(it.value.toString())
                logger.info("List of ${endpointName} ids:")
                //Use the utility function outputListIds to output the list of ids in a readable fashion
                Util.outputListIds(response.get(1), endpointName)
            } else {
                //If the response status is not 200 then log the error message
                logger.error(response.get(1).toString())
            }
        }

        //Get the created_at and updated_at values from the top-level organization details
        def createdDate = boomTownRoiResponse.get(1)['created_at']
        def updatedDate = boomTownRoiResponse.get(1)['updated_at']
        //Write a format to parse the date from the values
        def dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        //Parse the values into dates
        def parsedCreatedDate = new SimpleDateFormat(dateFormat).parse(createdDate.toString())
        def parsedUpdatedDate = new SimpleDateFormat(dateFormat).parse(updatedDate.toString())

        //If the updatedDate occurred after the createdDate then log a successful verification
        if (parsedUpdatedDate.after(parsedCreatedDate)) {
            logger.info("VERIFICATION SUCCESSFUL: updated_at(${updatedDate}) occurs later than created_at(${createdDate})")
        } else {
            //If the updatedDate does not occur after the createdDate then log a failed verification
            logger.error("VERIFICATION FAILED: updated_at(${updatedDate}) does not occur after created_at(${createdDate})")
        }

        //Get the repo_url from the top-level details response
        String reposUrl = boomTownRoiResponse.get(1)['repos_url']
        //Define a boolean and counters for a while loop
        boolean getNextReposPage = true
        int pageCounter = 1
        int repositoryCount = 0
        //while the boolean getNextReposPage is true
        while (getNextReposPage) {
            /*Submit a get request to the reposUrl. Use the page query parameter and the pageCounter to iterate through
             *all of the pages on each pass of the while loop.  Get the number of repositories returned in the get request.
             */
            def reposInResponse = Util.sendGetRequest("${reposUrl}?page=${pageCounter}").get(1).size()
            /*The maximum amount of repositories allowed in a single page is 30.  If there are less than 30 responses
             *on the current page then the current page is the last page, so the loop should be ended.
             */
            if (reposInResponse < 30) {
                //Add the count of repositories to the respositoryCounter variable
                repositoryCount += reposInResponse
                //End the loop because there will not be another page
                getNextReposPage = false
            } else {
                //Add the count of repositories to the respositoryCounter variable
                repositoryCount += reposInResponse
                //Increment the pageCounter variable
                pageCounter ++
            }
        }

        //Get the public_repos count from the top-level organization response
        def boomTownRoiPublicReposCount = boomTownRoiResponse.get(1)['public_repos']

        //If the public_repos count is equal to the repositoryCount then log a successful verification message
        if (boomTownRoiPublicReposCount == repositoryCount) {
            logger.info("VERIFICATION SUCCESSFUL: The count from public_repos(${boomTownRoiPublicReposCount.toString()}) gotten from /orgs/BoomTownROI matches the number of repositories returned from /orgs/BoomTownROI/repos(${repositoryCount}).")
        } else {
            //If the public_repos count is not equal to the repositoryCount then log a failed verification message
            logger.error("VERIFICATION FAILED: The count from public_repos(${boomTownRoiPublicReposCount.toString()}) gotten from /orgs/BoomTownROI does not match the number of repositories returned from /orgs/BoomTownROI/repos(${repositoryCount}).")
        }
    }
}
