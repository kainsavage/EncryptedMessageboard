/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.auth.entity;

import java.util.Map;

import net.teamclerks.em.EMApplication;

import com.google.common.collect.Maps;
import com.techempower.data.annotation.CachedEntity;
import com.techempower.gemini.pyxis.BasicSecurity;
import com.techempower.gemini.pyxis.BasicWebUser;
import com.techempower.js.legacy.Visitor;
import com.techempower.js.legacy.VisitorFactory;
import com.techempower.js.legacy.Visitors;

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
  public static final VisitorFactory<User> visitorFactory =
      new VisitorFactory<User>()
      {
        @Override
        public Visitor visitor(User object)
        {
          final Map<String,Object> json = Maps.newHashMap();

          json.put("firstname", object.getUserFirstname());
          json.put("lastname", object.getUserLastname());
          json.put("publickey", object.getPublicKey());
          
          return Visitors.forMaps().visitor(json);
        }
      };

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

}   // End User.
