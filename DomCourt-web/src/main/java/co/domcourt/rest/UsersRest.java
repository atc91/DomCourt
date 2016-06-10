package co.domcourt.rest;

import co.domcourt.entities.User;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import co.domcourt.users.UsersFacade;
import javax.ejb.EJB;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;

/**
 *
 * @author Christian
 */
@Stateless
@Path( "users" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class UsersRest
{
    @EJB
    private UsersFacade usersFacade;

    @POST
    public String signUp( User user )
    {
        return usersFacade.signUp( user );
    }

    @POST
    @Path( "signIn" )
    public String signIn( User user )
    {
        return usersFacade.signIn( user.getUsername(), user.getPassword() );
    }

    @PUT
    @Path( "changePassword" )
    public void changePassword( @HeaderParam( "Bearer" ) String token, String password )
    {
        usersFacade.changePassword( token, password );
    }

    @PUT
    @Path( "changeUsername" )
    public void changeUsername( @HeaderParam( "Bearer" ) String token, String username )
    {
        usersFacade.changeUsername( token, username );
    }
}
