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
@Table(name = "wp_termmeta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WpTermmeta.findAll", query = "SELECT w FROM WpTermmeta w"),
    @NamedQuery(name = "WpTermmeta.findByMetaId", query = "SELECT w FROM WpTermmeta w WHERE w.metaId = :metaId"),
    @NamedQuery(name = "WpTermmeta.findByTermId", query = "SELECT w FROM WpTermmeta w WHERE w.termId = :termId"),
    @NamedQuery(name = "WpTermmeta.findByMetaKey", query = "SELECT w FROM WpTermmeta w WHERE w.metaKey = :metaKey")})
public class WpTermmeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "meta_id")
    private Long metaId;
    @Basic(optional = false)
    @Column(name = "term_id")
    private long termId;
    @Column(name = "meta_key")
    private String metaKey;
    @Lob
    @Column(name = "meta_value")
    private String metaValue;

    public WpTermmeta() {
    }

    public WpTermmeta(Long metaId) {
        this.metaId = metaId;
    }

    public WpTermmeta(Long metaId, long termId) {
        this.metaId = metaId;
        this.termId = termId;
    }

    public Long getMetaId() {
        return metaId;
    }

    public void setMetaId(Long metaId) {
        this.metaId = metaId;
    }

    public long getTermId() {
        return termId;
    }

    public void setTermId(long termId) {
        this.termId = termId;
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
        if (!(object instanceof WpTermmeta)) {
            return false;
        }
        WpTermmeta other = (WpTermmeta) object;
        if ((this.metaId == null && other.metaId != null) || (this.metaId != null && !this.metaId.equals(other.metaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "crawler.WpTermmeta[ metaId=" + metaId + " ]";
    }
    
}
