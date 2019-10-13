/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contacts;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dennis
 */
@Entity
@Table(name = "COMMUNICATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Communication.findAll", query = "SELECT c FROM Communication c")
    , @NamedQuery(name = "Communication.findByEntryid", query = "SELECT c FROM Communication c WHERE c.entryid = :entryid")
    , @NamedQuery(name = "Communication.findByPersonid", query = "SELECT c FROM Communication c WHERE c.personid = :personid")
    , @NamedQuery(name = "Communication.findByType", query = "SELECT c FROM Communication c WHERE c.type = :type")
    , @NamedQuery(name = "Communication.findByValue", query = "SELECT c FROM Communication c WHERE c.value = :value")
    , @NamedQuery(name = "Communication.findByPreferred", query = "SELECT c FROM Communication c WHERE c.preferred = :preferred")})
public class Communication implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENTRYID")
    private Integer entryid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERSONID")
    private int personid;
    @Size(max = 10)
    @Column(name = "TYPE")
    private String type;
    @Size(max = 80)
    @Column(name = "VALUE")
    private String value;
    @Column(name = "PREFERRED")
    private Integer preferred;

    public Communication() {
    }

    public Communication(Integer entryid) {
        this.entryid = entryid;
    }

    public Communication(Integer entryid, int personid) {
        this.entryid = entryid;
        this.personid = personid;
    }

    public Integer getEntryid() {
        return entryid;
    }

    public void setEntryid(Integer entryid) {
        this.entryid = entryid;
    }

    public int getPersonid() {
        return personid;
    }

    public void setPersonid(int personid) {
        this.personid = personid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getPreferred() {
        return preferred;
    }

    public void setPreferred(Integer preferred) {
        this.preferred = preferred;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entryid != null ? entryid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Communication)) {
            return false;
        }
        Communication other = (Communication) object;
        if ((this.entryid == null && other.entryid != null) || (this.entryid != null && !this.entryid.equals(other.entryid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "contacts.Communication[ entryid=" + entryid + " ]";
    }
    
}
