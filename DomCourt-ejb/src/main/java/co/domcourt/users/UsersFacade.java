/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.domcourt.users;

import co.domcourt.entities.EntitiesFacade;
import co.domcourt.entities.User;
import co.domcourt.judges.JudgesFacade;
import co.domcourt.utils.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.security.Key;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Christian
 */
@Stateless
public class UsersFacade
{
    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    public static final byte[] SECRET = "HLr*RR-@4T'`HMBDM2C'jRvXe+`7~9V9pfVTsJ.4\"`<x6ub3N/u+/$t:.wa+;NH\"t}xXRUB4M~@\\qfY@Eqw:9[+L@UH.M{k\\trXX\\:7x}F?>$fDxL}<(_:XcU2VNA`Y*".getBytes();

    public static final Key SIGNING_KEY = new SecretKeySpec( SECRET, SIGNATURE_ALGORITHM.getJcaName() );

    @EJB
    private EntitiesFacade entitiesFacade;

    /**
     * Generate an expiration date adding 10 minutes to the system's current
     * date
     * time
     *
     * @return The expiration date
     */
    private static Date generateTokenExpiration()
    {
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.add( Calendar.HOUR, 12 );

        return calendar.getTime();
    }

    /**
     * Generate the access token and its expiration date in order to be used
     * within 10 minutes based on the system's current date time
     *
     * @param user The user who will be configured with his access properties
     */
    private String generateToken( User user )
    {
        JwtBuilder builder = Jwts.builder()
                .setId( user.getId() + "" )
                .setIssuedAt( GregorianCalendar.getInstance().getTime() )
                .setExpiration( generateTokenExpiration() )
                .signWith( SIGNATURE_ALGORITHM, SIGNING_KEY );

        return builder.compact();
    }

    /**
     * Create a new user on the system and set the session's access token
     * <p>
     * //TODO The new user is created with a new subscription binded to the
     * Expert Plan
     *
     * @param user The new user
     *
     * @return El token de acceso
     *
     */
    public String signUp( User user )
    {
        if( user == null )
        {
            throw new RuntimeException( "El usuario no tiene información completa" );
        }

        if( !Strings.isEmail( user.getEmail() )
            || !entitiesFacade.findBy( User.class, "email", user.getEmail() ).isEmpty() )
        {
            throw new RuntimeException( "Email invalido" );
        }

        entitiesFacade.create( user );

        return generateToken( user );
    }

    /**
     * Allow a user login on the system using his username (or email) and
     * password
     * <p>
     * Set the session's access token
     *
     * @param username The user's username
     * @param password The user's password
     *
     * @return El token de acceso
     */
    public String signIn( String username, String password )
    {
        if( Strings.isAnyEmptyOrNull( username, password ) )
        {
            throw new RuntimeException( "El usuario o la contraseña son invalidos" );
        }

        List<User> list = username.contains( "@" )
                          ? entitiesFacade.findBy( User.class, "email", username )
                          : entitiesFacade.findBy( User.class, "username", username );

        if( list.size() != 1 )
        {
            throw new RuntimeException( "No se encontró un usuario con username " + username );
        }

        User user = list.get( 0 );

        if( !Strings.areEquals( user.getPassword(), password ) )
        {
            throw new RuntimeException( "Contraseña erronea" );
        }

        return generateToken( user );
    }

    public User getUser( String token )
    {
        int userId;
        User user = null;

        try
        {
            Claims claims = Jwts.parser()
                    .setSigningKey( SECRET )
                    .parseClaimsJws( token ).getBody();

            userId = Integer.parseInt( claims.getId() );
        }
        catch( SignatureException e )
        {
            throw new RuntimeException( "Invalid token" );
        }
        catch( ExpiredJwtException e )
        {
            throw new RuntimeException( "Expired token" );
        }

        List<User> list = entitiesFacade.findBy( User.class, "id", userId );

        if( list.size() == 1 )
        {
            user = list.get( 0 );
        }

        if( user == null )
        {
            throw new RuntimeException( "Invalid access" );
        }

        return user;
    }

    /**
     * Allows to the user change his username
     *
     * @param token    the JSON Web Token
     * @param username the new username
     */
    public void changeUsername( String token, String username )
    {
        if( Strings.isTrimEmptyOrNull( username ) || username.contains( "@" ) )
        {
            throw new RuntimeException( "Nombre de usuario invalido no debe ser nulo ni vacio" );
        }

        User user = getUser( token );

        if( !entitiesFacade.filter( User.class,
                                    "SELECT u FROM User u WHERE u.id <> :id AND u.username = :username",
                                    new SimpleEntry<String, Object>( "id", user.getId() ),
                                    new SimpleEntry<String, Object>( "username", username ) ).isEmpty() )
        {
            throw new RuntimeException( "Nombre de usuario en uso" );
        }

        user.setUsername( username );

        entitiesFacade.edit( user );
    }

    /**
     * Allows the user change his password
     *
     * @param token    the JSON Web Token
     * @param password The new user's password
     */
    public void changePassword( String token, String password )
    {
        if( Strings.isTrimEmptyOrNull( password ) )
        {
            throw new RuntimeException( "La contraseña no debe ser nula ni vacia" );
        }

        User user = getUser( token );

        user.setPassword( password );

        entitiesFacade.edit( user );
    }

    public EntitiesFacade getEntitiesFacade()
    {
        return entitiesFacade;
    }

    public void setEntitiesFacade( EntitiesFacade entitiesFacade )
    {
        this.entitiesFacade = entitiesFacade;
    }
}
