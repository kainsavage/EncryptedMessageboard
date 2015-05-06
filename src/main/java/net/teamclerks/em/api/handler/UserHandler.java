/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.api.handler;

import java.util.Collection;
import java.util.Map;

import net.teamclerks.em.EMApplication;
import net.teamclerks.em.EMContext;
import net.teamclerks.em.api.entity.Friends;
import net.teamclerks.em.api.form.ProfileForm;
import net.teamclerks.em.api.form.RegistrationForm;
import net.teamclerks.em.auth.EMSecurity;
import net.teamclerks.em.auth.entity.User;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.collect.Maps;
import com.techempower.gemini.form.FormValidation;
import com.techempower.gemini.path.MethodUriHandler;
import com.techempower.gemini.path.annotation.Get;
import com.techempower.gemini.path.annotation.Path;
import com.techempower.gemini.path.annotation.Post;
import com.techempower.gemini.path.annotation.Put;
import com.techempower.js.JavaScriptWriter;
import com.techempower.js.legacy.LegacyJavaScriptWriter;

public class UserHandler
  extends MethodUriHandler<EMContext>
{
  private static final JavaScriptWriter UserWriter = LegacyJavaScriptWriter.custom()
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
  
  @Path("")
  @Get
  public boolean getUser()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    final User user = this.security.getUser(context());
    
    if(user != null)
    {
      json.put("id", user.getId());
      json.put("username", user.getUserUsername());
      json.put("firstname", user.getUserFirstname());
      json.put("lastname", user.getUserLastname());
      json.put("email", user.getUserEmail());
      json.put("friendsOnly", user.isFriendsOnlyMessaging());
      json.put("publicKey", user.getPublicKey());
    }
    
    return json(json);
  }
  
  @Path("")
  @Put
  public boolean editUser()
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
  
  @Path("")
  @Post
  public boolean registerUser()
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
  
  @Path("friends")
  @Get
  public boolean getFriends()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    final User user = this.security.getUser(context());
    if(user != null)
    {
      final Collection<User> friends = 
          (Collection<User>)application.getStore().<User,User>getRelation(Friends.class).rightValueList(user.getId());
      
      json.put("friends", friends);
    }
    
    return json(json);
  }
}
