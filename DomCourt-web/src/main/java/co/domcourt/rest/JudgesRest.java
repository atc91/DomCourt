package co.domcourt.rest;

import co.domcourt.entities.Judge;
import co.domcourt.entities.User;
import co.domcourt.judges.JudgesFacade;
import co.domcourt.users.UsersFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author 1010199039
 */
@Stateless
@Path( "judges" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class JudgesRest
{
    @EJB
    private JudgesFacade judgesFacade;

    @EJB
    private UsersFacade usersFacade;

    @POST
    @Path( "{judgeName}" )
    public String createJudge( @HeaderParam( "Bearer" ) String token,
                               @PathParam( "judgeName" ) String judgeName )
    {
        return judgesFacade.createJudge( usersFacade.getUser( token ), judgeName );
    }

    @GET
    @Path( "{judgeName}" )
    public String judgeUrl( @PathParam( "judgeName" ) String judgeName )
    {
        return judgesFacade.judgeUrl( judgeName );
    }

    @GET
    public List<Judge> listJudges( @HeaderParam( "Bearer" ) String token )
    {
        return judgesFacade.listJudges( usersFacade.getUser( token ) );
    }

    @DELETE
    @Path( "{judgeName}" )
    public void deleteJudge( @HeaderParam( "Bearer" ) String token,
                             @PathParam( "judgeName" ) String judgeName )
    {
        judgesFacade.deleteJudge( usersFacade.getUser( token ), judgeName );
    }

    @PUT
    @Path( "{judgeName}/password" )
    public void changeAdminPassword( @HeaderParam( "Bearer" ) String token,
                                     @PathParam( "judgeName" ) String judgeName,
                                     String password )
    {
        judgesFacade.changeAdminPassword( usersFacade.getUser( token ), judgeName, password );
    }

    @POST
    @Path( "{judgeName}/users" )
    public void addUser( @PathParam( "judgeName" ) String judgeName,
                         User user )
    {
        judgesFacade.addUser( judgeName, user );
    }

    public JudgesFacade getJudgesFacade()
    {
        return judgesFacade;
    }

    public void setJudgesFacade( JudgesFacade judgesFacade )
    {
        this.judgesFacade = judgesFacade;
    }

    public UsersFacade getUsersFacade()
    {
        return usersFacade;
    }

    public void setUsersFacade( UsersFacade usersFacade )
    {
        this.usersFacade = usersFacade;
    }
}
