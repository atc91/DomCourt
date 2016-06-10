/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.domcourt.judges;

import co.domcourt.entities.EntitiesFacade;
import co.domcourt.entities.Judge;
import co.domcourt.entities.User;
import co.domcourt.utils.Permission;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Christian
 */
@Stateless
public class JudgesFacade
{
    public static final String DOCKER_CA = "C:\\Users\\Christian\\.docker\\machine\\certs\\ca.pem";

    public static final String DOCKER_CERT = "C:\\Users\\Christian\\.docker\\machine\\certs\\cert.pem";

    public static final String DOCKER_KEY = "C:\\Users\\Christian\\.docker\\machine\\certs\\key.pem";

    public static final String DOCKER_HOST = "192.168.99.100";

    public static final String DOCKER_PORT = "2376";

    //Windows Prefix
//    public static final String PREFIX_DOCKER = String.format( "docker --tlsverify --tlscacert=%s --tlscert=%s --tlskey=%s -H %s:%s",
//                                                              DOCKER_CA, DOCKER_CERT, DOCKER_KEY, DOCKER_HOST, DOCKER_PORT );
    //Docker Container Prefix
    public static final String PREFIX_DOCKER = "docker";

    public static final String CMD_CREATE = String.format( "%s run -d -P --name %s domcourt/domjudge",
                                                           PREFIX_DOCKER, "%s" );

    public static final String CMD_PORT = String.format( "%s  port %s %s",
                                                         PREFIX_DOCKER, "%s", "%s" );

    public static final String CMD_REMOVE = String.format( "%s rm -f %s",
                                                           PREFIX_DOCKER, "%s" );

    public static final String PREFIX_MYSQL = "mysql -u root -proot -D domjudge -e";

    public static final String CMD_PASSWORD = String.format( "%s \"UPDATE user SET password='%s' WHERE username = '%s';",
                                                             PREFIX_MYSQL, "%2$s", "%1$s" );

    public static final String CMD_TEAM = String.format( "%s INSERT INTO team (name, categoryid) VALUES ('%s', '3');"
                                                         + "INSERT INTO user (username, name, password, teamid) "
                                                         + "VALUES ('%s', '%s', '%s', (SELECT teamid FROM team WHERE name = '%s' ) )",
                                                         PREFIX_MYSQL, "%1$s", "%1$s", "%1$s", "%2$s", "%1$s" ); //user - pass

    @EJB
    private EntitiesFacade entitiesFacade;

    private static String execute( String format, Object... args )
    {
        StringBuilder result = new StringBuilder();

        try
        {
            Process p = Runtime.getRuntime().exec( String.format( format, args ) );

            BufferedReader pstdout = new BufferedReader( new InputStreamReader( p.getInputStream() ) );

            String line;
            while( ( line = pstdout.readLine() ) != null )
            {
                result.append( line ).append( "\n" );
            }

            p.waitFor();
        }
        catch( Exception ex )
        {
            Logger.getLogger( JudgesFacade.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return result.toString();
    }

    private static String md5Hex( String data ) throws NoSuchAlgorithmException
    {
        return new BigInteger( 1, MessageDigest.getInstance( "MD5" ).digest( data.getBytes() ) ).toString( 16 );
    }

    public String createJudge( User user, String judgeName )
    {
        String containerId = execute( CMD_CREATE, judgeName ).trim();

        if( containerId.isEmpty() )
        {
            throw new RuntimeException( "Error" );
        }

        int httpPort = Integer.parseInt( execute( CMD_PORT, judgeName, 80 ).split( ":" )[1].trim() );

        Judge judge = new Judge();
        judge.setContainerId( containerId );
        judge.setName( judgeName );
        judge.setUser( user );

        entitiesFacade.create( judge );

        return String.format( "%s:%s/domjudge",
                              DOCKER_HOST,
                              httpPort );
    }

    public String judgeUrl( String judgeName )
    {
        int httpPort = Integer.parseInt( execute( CMD_PORT, judgeName, 80 ).split( ":" )[1].trim() );

        return String.format( "%s:%s/domjudge",
                              DOCKER_HOST,
                              httpPort );
    }

    public void deleteJudge( User user, String judgeName )
    {
        Judge judge = entitiesFacade.findBy( Judge.class, "name", judgeName ).get( 0 );

        if( !authorization( user, judge ).canDelete() )
        {
            throw new RuntimeException( "No access" );
        }

        execute( CMD_REMOVE, judgeName );

        entitiesFacade.remove( judge );
    }

    public void addUser( String judgeName, User member )
    {
        try
        {
            String password = md5Hex( String.format( "%s#%s", member.getUsername(), member.getPassword() ) );

            execute( CMD_TEAM, member.getUsername(), password );
        }
        catch( Exception ex )
        {
            new RuntimeException( ex );
        }
    }

    public List<Judge> listJudges( User user )
    {
        return user.getJudgeList();
    }

    public void changeAdminPassword( User user, String judgeName, String pass )
    {
        Judge judge = entitiesFacade.findBy( Judge.class, "name", judgeName ).get( 0 );

        if( !authorization( user, judge ).canUpdate() )
        {
            throw new RuntimeException( "No access" );
        }

        try
        {
            //Update admin password of the current judge
            execute( String.format( CMD_PASSWORD,
                                    md5Hex( pass + "#" + pass ),
                                    "admin" ) );
        }
        catch( NoSuchAlgorithmException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    protected Permission authorization( User user, Judge judge )
    {
        Permission permission = Permission.NONE;

        if( judge.getUser().equals( user ) )
        {
            permission = Permission.FULL;
        }

        return permission;
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
