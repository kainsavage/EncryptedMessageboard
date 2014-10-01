/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.api.handler;

import java.util.*;

import net.teamclerks.em.*;
import net.teamclerks.em.api.form.*;
import net.teamclerks.em.auth.*;
import net.teamclerks.em.auth.entity.*;

import org.apache.commons.lang3.*;

import com.google.common.collect.*;
import com.techempower.gemini.form.*;
import com.techempower.gemini.path.*;
import com.techempower.gemini.path.annotation.*;

public class ProfileHandler
  extends MethodPathHandler<EMContext>
{
  private final EMApplication application;
  private final EMSecurity    security;
  
  public ProfileHandler(EMApplication application)
  {
    super(application);
    
    this.application = application;
    this.security = this.application.getSecurity();
  }
  
  @PathSegment
  public boolean save()
  {
    final Map<String,Object> json = Maps.newHashMap();

    final User user = this.security.getUser(context()); 
    
    if(user != null)
    {
      ProfileForm form = new ProfileForm(this.application);
      form.setValues(context());
      
      FormValidation validation = form.validate(context());
      
      if(validation.hasErrors())
      {
        json.put("validation", validation);
        json.put("success", false);
      }
      else
      {
        // Success
        user.setPublicKey(StringEscapeUtils.unescapeHtml4(form.publicKey.getStringValue()));
        user.setFriendsOnlyMessaging(form.friendsOnly.isChecked());
        user.setUserFirstname(form.firstName.getStringValue().trim());
        user.setUserLastname(form.lastName.getStringValue().trim());
        user.setUserEmail(form.email.getStringValue());
        
        this.application.getStore().put(user);
        
        json.put("success", true);
      }
    }
    
    return json(json);
  }
}
