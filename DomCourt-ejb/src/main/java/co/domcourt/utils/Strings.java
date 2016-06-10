package co.domcourt.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 *
 * @author Christian
 */
public class Strings
{
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static boolean areEquals( String s1, String s2 )
    {
        if( s1 != null )
        {
            return s1.equals( s2 );
        }

        return false;
    }

    public static boolean isAnyEmpty( String... strings )
    {
        for( String s : strings )
        {
            if( isEmpty( s ) )
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isAnyEmptyOrNull( String... strings )
    {
        for( String s : strings )
        {
            if( isEmptyOrNull( s ) )
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isAnyNull( String... strings )
    {
        for( String s : strings )
        {
            if( isNull( s ) )
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isAnyTrimEmpty( String... strings )
    {
        for( String s : strings )
        {
            if( isTrimEmpty( s ) )
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isAnyTrimEmptyOrNull( String... strings )
    {
        for( String s : strings )
        {
            if( isTrimEmptyOrNull( s ) )
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmail( String s )
    {
        return !isEmptyOrNull( s ) && s.matches( "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" );
    }

    public static boolean isEmpty( String s )
    {
        return s.length() == 0;
    }

    public static boolean isEmptyOrNull( String s )
    {
        return s == null ? true : s.length() == 0;
    }

    public static boolean isNull( String s )
    {
        return s == null;
    }

    public static boolean isTrimEmpty( String s )
    {
        return isEmpty( s.trim() );
    }

    public static boolean isTrimEmptyOrNull( String s )
    {
        return s == null ? true : isTrimEmpty( s );
    }

    public static String random( int length, String charset )
    {
        StringBuilder sb = new StringBuilder( length );

        for( int i = 0; i < length; i++ )
        {
            sb.append( charset.charAt( SECURE_RANDOM.nextInt( charset.length() ) ) );
        }

        return sb.toString();
    }

    public static String randomAlpahnumeric( int length )
    {
        return random( length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" );
    }

    public static String randomUUID()
    {
        return UUID.randomUUID().toString().replace( "-", "" );
    }
}
