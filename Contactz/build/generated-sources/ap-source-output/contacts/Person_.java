package contacts;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-10-12T21:24:42")
@StaticMetamodel(Person.class)
public class Person_ { 

    public static volatile SingularAttribute<Person, String> firstname;
    public static volatile SingularAttribute<Person, String> gender;
    public static volatile SingularAttribute<Person, Date> dob;
    public static volatile SingularAttribute<Person, Integer> personid;
    public static volatile SingularAttribute<Person, String> title;
    public static volatile SingularAttribute<Person, String> lastname;

}