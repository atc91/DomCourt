/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.domcourt.utils;

/**
 * Services for permission managment on ems api.
 * <p>
 * This system mimics the linux file permissions system.( More info:
 * https://ubunturoot.wordpress.com/2007/12/07/permisos-en-linux-con-chmod/ )
 * <p>
 * @author Christian
 */
public enum Permission
{
    /**
     * No access
     */
    NONE( 0 ),
    /**
     * Delete access only
     */
    D( 1 ),
    /**
     * Update access only
     */
    U( 2 ),
    /**
     * Update and Delete access only
     */
    UD( 3 ),
    /**
     * Read access only
     */
    R( 4 ),
    /**
     * Read and delete access only
     */
    RD( 5 ),
    /**
     * Read and update access only
     */
    RU( 6 ),
    /**
     * Read, update and delete access only
     */
    RUD( 7 ),
    /**
     * Create access only
     */
    C( 8 ),
    /**
     * Create and delete access only
     */
    CD( 9 ),
    /**
     * Create and update access only
     */
    CU( 10 ),
    /**
     * Create, update and delete access only
     */
    CUD( 11 ),
    /**
     * Create and read access only
     */
    CR( 12 ),
    /**
     * Create, read and delete access only
     */
    CRD( 13 ),
    /**
     * Create, read and update access only
     */
    CRU( 14 ),
    /**
     * Create, read, update and delete access
     */
    FULL( 15 );

    private final int functions;

    private Permission( int functions )
    {
        this.functions = functions;
    }

    public boolean canCreate()
    {
        return ( ( 1 << 3 ) & functions ) != 0;
    }

    public boolean canRead()
    {
        return ( ( 1 << 2 ) & functions ) != 0;
    }

    public boolean canUpdate()
    {
        return ( ( 1 << 1 ) & functions ) != 0;
    }

    public boolean canDelete()
    {
        return ( 1 & functions ) != 0;
    }
}
