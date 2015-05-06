/**
 * Copyright (c) 2015
 * TechEmpower, Inc.
 * All Rights Reserved.
 *
 * TestApplication
 */

package net.teamclerks.em.auth;

import com.techempower.gemini.Context;
import com.techempower.gemini.ResponseCookie;
import com.techempower.gemini.prehandler.Prehandler;
import com.techempower.helper.StringHelper;

public class EMDeliverTokenPrehandler implements Prehandler
{
  @Override
  public boolean prehandle(Context context)
  {
    if(!context.session().has("XSRF-TOKEN")){
      String csrf = StringHelper.randomString.alphanumeric(16);
      context.session().put("XSRF-TOKEN", csrf);
      ResponseCookie cookie = new ResponseCookie("XSRF-TOKEN", csrf);
      cookie.setPath("/");
      context.cookies().put(cookie);
    }
    return false;
  }
}
 