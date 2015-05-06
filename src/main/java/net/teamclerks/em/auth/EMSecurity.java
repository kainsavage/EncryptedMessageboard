/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.auth;

import net.teamclerks.em.auth.entity.*;

import com.techempower.gemini.*;
import com.techempower.gemini.pyxis.*;

/**
 * EMSecurity provides Pyxis-based security services for the
 * EncryptedMessageboard application.
 *
 * Development history:
 *   2013-10-07 - ms - Created
 *
 * @author (username)
 */
public class EMSecurity
     extends BasicSecurity<User, Group>
{

  //
  // Member methods.
  //

  /**
   * Constructor.
   */
  public EMSecurity(GeminiApplication application)
  {
    super(application, User.class, Group.class, UserToGroup.class);
  }

  /**
   * Gets the logged-in user from the Context's session.  Returns null
   * if no user is logged in.
   *
   * @param Context the Context from which to retrieve a user.
   */
  @Override
  public User getUser(Context context)
  {
    return (User)super.getUser(context);
  }
  
  @Override
  public User constructUser()
  {
    return new User(this);
  }

}   // End EMSecurity.
