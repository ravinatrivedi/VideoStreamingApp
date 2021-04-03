/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment1soapservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aunsha Asaithambi
 */
@Entity
@Table(name = "SHOWS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Shows.findAll", query = "SELECT s FROM Shows s"),
    @NamedQuery(name = "Shows.findById", query = "SELECT s FROM Shows s WHERE s.id = :id"),
    @NamedQuery(name = "Shows.findByTitle", query = "SELECT s FROM Shows s WHERE s.title = :title"),
    @NamedQuery(name = "Shows.findByDescription", query = "SELECT s FROM Shows s WHERE s.description = :description"),
    @NamedQuery(name = "Shows.findByCast", query = "SELECT s FROM Shows s WHERE s.cast = :cast"),
    @NamedQuery(name = "Shows.findByDirector", query = "SELECT s FROM Shows s WHERE s.director = :director"),
    @NamedQuery(name = "Shows.findByYearOfRelease", query = "SELECT s FROM Shows s WHERE s.yearOfRelease = :yearOfRelease")})
public class Shows implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 20)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 225)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 225)
    @Column(name = "CAST")
    private String cast;
    @Size(max = 225)
    @Column(name = "DIRECTOR")
    private String director;
    @Size(max = 10)
    @Column(name = "YEAR_OF_RELEASE")
    private String yearOfRelease;
    @Lob
    @Column(name = "THUMBNAIL")
    private byte[] thumbnail;
    @JoinColumn(name = "CATEGORYID", referencedColumnName = "ID")
    @ManyToOne
    private Category categoryid;
    @JoinColumn(name = "TYPEID", referencedColumnName = "ID")
    @ManyToOne
    private Type typeid;

    public Shows() {
    }

    public Shows(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(String yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Category getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Category categoryid) {
        this.categoryid = categoryid;
    }

    public Type getTypeid() {
        return typeid;
    }

    public void setTypeid(Type typeid) {
        this.typeid = typeid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Shows)) {
            return false;
        }
        Shows other = (Shows) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.assignment1soapservice.entities.Shows[ id=" + id + " ]";
    }
    
}
