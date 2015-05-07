/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.auth.entity;

import java.util.*;

import net.teamclerks.em.*;

import com.techempower.collection.*;
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
  private String  publicKey;
  private boolean friendsOnlyMessaging;

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
  
  public boolean isFriendsOnlyMessaging()
  {
    return this.friendsOnlyMessaging;
  }
  
  public void setFriendsOnlyMessaging(boolean friendsOnlyMessaging)
  {
    this.friendsOnlyMessaging = friendsOnlyMessaging;
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
  
  /**
   * Returns a simple view map.
   */
  public final Map<String,Object> view()
  {
    return new MutableNamedObjects()
      .put("id", this.getId())
      .put("username", this.getUserUsername())
      .put("firstname", this.getUserFirstname())
      .put("lastname", this.getUserLastname())
      .put("email", this.getUserEmail())
      .put("friendsOnly", this.isFriendsOnlyMessaging())
      .put("publicKey", this.getPublicKey())
      .asMap();
  }

}   // End User.
