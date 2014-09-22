/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.admin.handler;

import net.teamclerks.em.*;
import net.teamclerks.em.auth.entity.*;

import com.techempower.gemini.*;
import com.techempower.gemini.admin.*;
import com.techempower.gemini.admin.standard.cache.*;
import com.techempower.gemini.admin.standard.config.*;
import com.techempower.gemini.admin.standard.email.*;
import com.techempower.gemini.admin.standard.monitor.*;
import com.techempower.gemini.admin.standard.system.*;

/**
 * EMGeminiAdminHandler is EncryptedMessageboard's implementation of Gemini's
 * basic admin handler.
 *
 * Development history:
 *   2013-10-07 - ms - Created
 *
 * @author (username)
 */
public class EMGeminiAdminHandler
     extends BasicAdminHandler<EMContext>
{

  //
  // Constants.
  //

  // An example of custom user list columns:
  /*
  ReflectiveListColumn[] CUSTOM_USER_LIST_COLUMNS = ReflectiveListColumn.constructArray(
  new String[] { "getFavoriteColor", "Favorite Color", "--"
               }
  );
  */

  //
  // Member methods.
  //

  /**
   * Constructor.
   */
  public EMGeminiAdminHandler(GeminiApplication application)
  {
    super(application, null, User.class, Group.class);

    // User management.
    addFunctionCategory(new EMUserCategory());

    // Email.
    addFunctionCategory(new EmailCategory());

    // Configuration.
    addFunctionCategory(new ConfigurationCategory());

    // Monitoring.
    addFunctionCategory(new MonitorCategory());

    // Cache relations.
    addFunctionCategory(new CacheCategory());

    // System management.
    addFunctionCategory(new SystemCategory());

    // Enable the log monitor (requires gemini-resin).
    //LogMonitor logMonitor = new LogMonitor(application, new ResinAdapter());
    //logMonitor.addChannel(new LogMonitorChannel("All", LogLevel.MINIMUM, LogLevel.MAXIMUM, null));
    //logMonitor.addChannel(new LogMonitorChannel("Notice and above", LogLevel.NOTICE, LogLevel.MAXIMUM, null));
    //addFunction(logMonitor);
  }

}   // End AdminHandler.

