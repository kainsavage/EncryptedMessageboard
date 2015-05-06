/**
 * Copyright (c) 2015
 * TechEmpower, Inc.
 * All Rights Reserved.
 *
 * TestApplication
 */

package net.teamclerks.em.auth;

import com.techempower.gemini.Context;
import com.techempower.gemini.prehandler.Prehandler;

public class EMXSRFPrehandler implements Prehandler
{

  @Override
  public boolean prehandle(Context context)
  {
    if(context.isPost() || context.isPut() || context.isDelete())
    {
      if (context.session().has("XSRF-TOKEN")) {
        if (context.getRequest().getCookie("XSRF-TOKEN") != null)
        {
          String cookieValue = context.getRequest().getCookie("XSRF-TOKEN").getValue();
          final String sessionValue = context.session().get("XSRF-TOKEN");
          if(sessionValue.equals(cookieValue))
          {
            return false;
          }
        }
      }
      context.setStatus(400);
      return true;
    }
      
    return false;
  }
}
 