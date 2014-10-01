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

import com.google.common.collect.*;
import com.techempower.gemini.form.*;
import com.techempower.gemini.path.*;
import com.techempower.gemini.path.annotation.*;
import com.techempower.js.*;

public class UserHandler
  extends MethodPathHandler<EMContext>
{
  private static final JavaScriptWriter UserWriter = JavaScriptWriter.custom()
      .addVisitorFactory(
          User.class, User.visitorFactory)
      .build();
  
  private final EMApplication application;
  private final EMSecurity security;
  
  public UserHandler(EMApplication application)
  {
    super(application, "UserHandler", UserWriter);
    
    this.application = application;
    this.security = this.application.getSecurity();
  }
  
  @PathSegment
  public boolean register()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    final RegistrationForm form = new RegistrationForm(this.application);
    
    form.setValues(context());
    
    final FormValidation validation = form.validate(context());
    
    if( validation.hasErrors() )
    {
      json.put("success", false);
      json.put("validation", validation);
    }
    else
    {
      // Success!
      User user = this.security.constructUser();
      user.initialize();
      user.setUserUsername(form.username.getStringValue());
      user.setUserPassword(form.password.getStringValue());

      application.getStore().put(user);
      application.getSecurity().login(context(), user, false);
      
      json.put("success", true);
      json.put("redirect", "/profile");
    }
    
    return json(json);
  }

  @PathSegment
  public boolean login()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    final LoginForm form = new LoginForm(context());
    
    form.setValues(context());

    final FormValidation formValidation  = form.validate(context());

    // Username and password provided.
    if ( formValidation.isGood() )
    {
      // Login successful.
      json.put("success", true);
      json.put("redirect",form.values().get("lhredirect"));
    }
    // Form not complete.  Include the Validation.
    json.put("validation", formValidation);
    
    return json(json);
  }
  
  @PathSegment
  public boolean logout()
  {
    final Map<String,Object> json = Maps.newHashMap();

    final User user = this.security.getUser(context()); 
    
    if(user != null)
    {
      this.security.logout(context());
    }
    
    return json(json);
  }
  
  @PathSegment
  public boolean auth()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    final User user = this.security.getUser(context());
    
    if(user != null)
    {
      final Map<String,Object> currentUser = Maps.newHashMap();
      currentUser.put("userUsername", user.getUserUsername());
      currentUser.put("userFirstname", user.getUserFirstname());
      currentUser.put("userLastname", user.getUserLastname());
      currentUser.put("userEmail", user.getUserEmail());
      currentUser.put("friendsOnly", user.isFriendsOnlyMessaging());
      currentUser.put("userPublicKey", user.getPublicKey());
      
      json.put("currentUser", currentUser);
    }
    
    return json(json);
  }
  
  @PathSegment
  public boolean friends()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    final User user = this.security.getUser(context());
    if(user != null)
    {
      final Collection<User> friends = 
          ((EMStore)application.getStore()).friends.rightValueList(user);
      
      json.put("friends", friends);
    }
    
    return json(json);
  }
}
