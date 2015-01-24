package de.bht.fpa.mail.s791831.model.data;


import java.io.File;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


/**
 * This class represents an IMAP account.
 * 
 * 
 * @author Siamak Haschemi, changed by Simone Strippgen
 */

@Entity
public class Account implements Serializable {
  
    @Id
    @GeneratedValue
    private long id;
    
//  private static final long serialVersionUID = -7660640539811469762L;

  private String name;

  private String host;

  private String username;

  private String password;
  
  @OneToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
  private Folder top = null;
  
  public Account() { 
      this.top = new Folder(new File(""), true);
  }

  public Account(String name, String host, String username, String password) {
      this.name = name;
      this.host = host;
      this.username = username;
      this.password = password;
      this.top = new Folder(new File(name), true);
  }


  public String getName() {
    return name;
  }

  public void setName(String fullName) {
    this.name = fullName;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

    public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("[Account: ");
    s.append("name=").append(name).append(" ");
    s.append("host=").append(host).append(" ");
    s.append("username=").append(username).append(" ");
    s.append("password=").append(password).append(" ");
    s.append("top folder=(").append(top).append(" ");
    s.append(")");
    s.append("]").append(" ");
    return s.toString();
  }

    public Folder getTop() {
        return top;
    }
    
    public void setTop(Folder top) {
        this.top = top;
    }

}
