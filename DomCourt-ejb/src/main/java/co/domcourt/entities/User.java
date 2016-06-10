/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.domcourt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Christian
 */
@Entity
@NamedQueries( 
{
    @NamedQuery( name = "User.findAll", query = "SELECT u FROM User u" )
} )
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    private Integer id;

    @Basic( optional = false )
    private String username;

    @Basic( optional = false )
    private String password;

    @Basic( optional = false )
    private String email;

    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "user" )
    private List<Judge> judgeList;

    public User()
    {
    }

    public User( Integer id )
    {
        this.id = id;
    }

    public User( Integer id, String username, String password, String email )
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public List<Judge> getJudgeList()
    {
        return judgeList;
    }

    public void setJudgeList( List<Judge> judgeList )
    {
        this.judgeList = judgeList;
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
        if( !( object instanceof User ) )
        {
            return false;
        }
        User other = (User) object;
        if( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "co.domcourt.entities.User[ id=" + id + " ]";
    }
    
}
