/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hohuy.crawler.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dinhtrung
 */
@Entity
@Table(name = "wp_postmeta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WpPostmeta.findAll", query = "SELECT w FROM WpPostmeta w"),
    @NamedQuery(name = "WpPostmeta.findByMetaId", query = "SELECT w FROM WpPostmeta w WHERE w.metaId = :metaId"),
    @NamedQuery(name = "WpPostmeta.findByPostId", query = "SELECT w FROM WpPostmeta w WHERE w.postId = :postId"),
    @NamedQuery(name = "WpPostmeta.findByMetaKey", query = "SELECT w FROM WpPostmeta w WHERE w.metaKey = :metaKey")})
public class WpPostmeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "meta_id")
    private Long metaId;
    @Basic(optional = false)
    @Column(name = "post_id")
    private long postId;
    @Column(name = "meta_key")
    private String metaKey;
    @Lob
    @Column(name = "meta_value")
    private String metaValue;

    public WpPostmeta() {
    }

    public WpPostmeta(Long metaId) {
        this.metaId = metaId;
    }

    public WpPostmeta(Long metaId, long postId) {
        this.metaId = metaId;
        this.postId = postId;
    }

    public Long getMetaId() {
        return metaId;
    }

    public void setMetaId(Long metaId) {
        this.metaId = metaId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getMetaKey() {
        return metaKey;
    }

    public void setMetaKey(String metaKey) {
        this.metaKey = metaKey;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (metaId != null ? metaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WpPostmeta)) {
            return false;
        }
        WpPostmeta other = (WpPostmeta) object;
        if ((this.metaId == null && other.metaId != null) || (this.metaId != null && !this.metaId.equals(other.metaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "crawler.WpPostmeta[ metaId=" + metaId + " ]";
    }
    
}
