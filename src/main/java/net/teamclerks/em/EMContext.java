/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em;

import com.techempower.gemini.*;

/**
 * EncryptedMessageboard specialization of Context.
 *
 * Development history:
 *   2013-10-07 - ms - Created
 *
 * @author (username)
 */
public class EMContext
     extends ResinContext
{

  //
  // Member variables.
  //

  //
  // Member methods.
  //

  /**
   * Standard constructor.
   *
   * @param request the Request object received by the Servlet.
   * @param application the Application reference.
   */
  public EMContext(Request request,
      GeminiApplication application)
  {
    super(request, application);
  }

  @Override
  public EMApplication getApplication()
  {
    return (EMApplication)super.getApplication();
  }

}   // End EMContext.

