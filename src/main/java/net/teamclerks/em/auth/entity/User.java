/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.auth.entity;

import net.teamclerks.em.*;

import com.techempower.data.annotation.*;
import com.techempower.gemini.pyxis.*;

/**
 * Represents a user of the EncryptedMessageboard application.  Basic attributes
 * such as username, e-mail address, and password are inherited from
 * BasicWebUser.
 *
 * Development history:
 *   2013-10-07 - ms - Created
 *
 * @author (username)
 */
@CachedEntity
public class User
     extends BasicWebUser
{

  //
  // Member variables.
  //
  private String publicKey;

  //
  // Member methods.
  //

  public String getPublicKey()
  {
    return publicKey;
  }

  public void setPublicKey(String publicKey)
  {
    this.publicKey = publicKey;
  }

  /**
   * Constructor.
   */
  public User()
  {
    super(EMApplication.getInstance().getSecurity());
  }
  
  public User(BasicSecurity<User, Group> security)
  {
    super(security);
  }

}   // End User.
