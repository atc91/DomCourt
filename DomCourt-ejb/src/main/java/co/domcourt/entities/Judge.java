/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.domcourt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Christian
 */
@Entity
@NamedQueries( 
{
    @NamedQuery( name = "Judge.findAll", query = "SELECT j FROM Judge j" )
} )
public class Judge implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    private Integer id;

    @Basic( optional = false )
    private String containerId;

    @Basic( optional = false )
    private String name;

    @JsonIgnore
    @JoinColumn( name = "user", referencedColumnName = "id" )
    @ManyToOne( optional = false )
    private User user;

    public Judge()
    {
    }

    public Judge( Integer id )
    {
        this.id = id;
    }

    public Judge( Integer id, String containerId, String name )
    {
        this.id = id;
        this.containerId = containerId;
        this.name = name;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getContainerId()
    {
        return containerId;
    }

    public void setContainerId( String containerId )
    {
        this.containerId = containerId;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( id != null ? id.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !( object instanceof Judge ) )
        {
            return false;
        }
        Judge other = (Judge) object;
        if( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "co.domcourt.entities.Judge[ id=" + id + " ]";
    }
    
}
