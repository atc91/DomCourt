/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.domcourt.entities;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * <p>
 * @author Christian
 */
@Stateless
public class EntitiesFacade
{

    @PersistenceContext( unitName = "domcourt-ejbPU" )
    private EntityManager em;

    public <T> void create( T entity )
    {
        getEntityManager().persist( entity );
    }

    public <T> void edit( T entity )
    {
        getEntityManager().merge( entity );
    }

    public <T> void remove( T entity )
    {
        getEntityManager().remove( getEntityManager().merge( entity ) );
    }

    public <T> List<T> filter( Class<T> entityClass, String query, SimpleEntry<String, Object>... parameters )
    {
        TypedQuery<T> typedQuery = getEntityManager().createQuery( query, entityClass );

        for( SimpleEntry<String, Object> param : parameters )
        {
            typedQuery.setParameter( param.getKey(), param.getValue() );
        }

        return typedQuery.getResultList();
    }

    public <T> T find( Class<T> entityClass, Object id )
    {
        return getEntityManager().find( entityClass, id );
    }

    public <T> List<T> findBy( Class<T> entityClass, String property, Object value )
    {
        return filter( entityClass,
                       "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + property + " = :value",
                       new SimpleEntry<>( "value", value ) );
    }

    public <T> List<T> findAll( Class<T> entityClass )
    {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select( cq.from( entityClass ) );
        return getEntityManager().createQuery( cq ).getResultList();
    }

    public <T> List<T> findRange( Class<T> entityClass, int[] range )
    {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select( cq.from( entityClass ) );
        Query q = getEntityManager().createQuery( cq );
        q.setMaxResults( range[1] - range[0] + 1 );
        q.setFirstResult( range[0] );
        return q.getResultList();
    }

    public <T> int count( Class<T> entityClass )
    {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from( entityClass );
        cq.select( getEntityManager().getCriteriaBuilder().count( rt ) );
        Query q = getEntityManager().createQuery( cq );
        return ( (Long) q.getSingleResult() ).intValue();
    }

    public EntityManager getEntityManager()
    {
        return em;
    }

    public void setEntityManager( EntityManager entityManager )
    {
        this.em = entityManager;
    }
}
