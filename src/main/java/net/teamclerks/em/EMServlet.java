/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em;

import com.techempower.gemini.transport.*;

/**
 * Servlet interface to the TemplateReskin application.  The Servlet container
 * (e.g., Resin) will load this Servlet and route inbound HTTP requests here.
 * From here, the Servlet will route requests to the Dispatcher, which in turn
 * routes requests to Handlers based on the request URI.
 *
 * Development history:
 *   2013-10-07 - ms - Created
 *
 * @author (username)
 */
@SuppressWarnings("serial")
public class EMServlet
     extends InfrastructureServlet
{

  //
  // Public member methods.
  //

  @Override
  public EMApplication getApplication()
  {
    return EMApplication.getInstance();
  }

}   // End EMServlet.

