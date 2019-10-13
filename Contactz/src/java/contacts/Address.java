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
@Table(name = "ADDRESS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a")
    , @NamedQuery(name = "Address.findByEntryid", query = "SELECT a FROM Address a WHERE a.entryid = :entryid")
    , @NamedQuery(name = "Address.findByPersonid", query = "SELECT a FROM Address a WHERE a.personid = :personid")
    , @NamedQuery(name = "Address.findByType", query = "SELECT a FROM Address a WHERE a.type = :type")
    , @NamedQuery(name = "Address.findByNumber", query = "SELECT a FROM Address a WHERE a.number = :number")
    , @NamedQuery(name = "Address.findByStreet", query = "SELECT a FROM Address a WHERE a.street = :street")
    , @NamedQuery(name = "Address.findByUnit", query = "SELECT a FROM Address a WHERE a.unit = :unit")
    , @NamedQuery(name = "Address.findByCity", query = "SELECT a FROM Address a WHERE a.city = :city")
    , @NamedQuery(name = "Address.findByState", query = "SELECT a FROM Address a WHERE a.state = :state")
    , @NamedQuery(name = "Address.findByZipcode", query = "SELECT a FROM Address a WHERE a.zipcode = :zipcode")})
public class Address implements Serializable {

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
    @Size(max = 40)
    @Column(name = "TYPE")
    private String type;
    @Size(max = 20)
    @Column(name = "NUMBER")
    private String number;
    @Size(max = 40)
    @Column(name = "STREET")
    private String street;
    @Size(max = 20)
    @Column(name = "UNIT")
    private String unit;
    @Size(max = 40)
    @Column(name = "CITY")
    private String city;
    @Size(max = 4)
    @Column(name = "STATE")
    private String state;
    @Size(max = 10)
    @Column(name = "ZIPCODE")
    private String zipcode;

    public Address() {
    }

    public Address(Integer entryid) {
        this.entryid = entryid;
    }

    public Address(Integer entryid, int personid) {
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.entryid == null && other.entryid != null) || (this.entryid != null && !this.entryid.equals(other.entryid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "contacts.Address[ entryid=" + entryid + " ]";
    }
    
}
