/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.api.handler;

import java.util.Map;

import net.teamclerks.em.EMApplication;
import net.teamclerks.em.EMContext;
import net.teamclerks.em.api.form.ProfileForm;
import net.teamclerks.em.auth.EMSecurity;
import net.teamclerks.em.auth.entity.User;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.collect.Maps;
import com.techempower.gemini.form.FormValidation;
import com.techempower.gemini.path.MethodUriHandler;
import com.techempower.gemini.path.annotation.Path;
import com.techempower.gemini.path.annotation.Post;

public class ProfileHandler
  extends MethodUriHandler<EMContext>
{
  private final EMApplication application;
  private final EMSecurity    security;
  
  public ProfileHandler(EMApplication application)
  {
    super(application);
    
    this.application = application;
    this.security = this.application.getSecurity();
  }
  
  @Path("")
  @Post
  public boolean saveProfile()
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
