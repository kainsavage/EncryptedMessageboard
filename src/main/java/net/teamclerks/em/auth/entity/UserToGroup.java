/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */
 
package net.teamclerks.em.auth.entity;

import com.techempower.data.annotation.*;

/**
 * Represents a user group relation for the EncryptedMessageboard application.
 *
 * @author (username)
 *
 * Development history:
 *   2013-10-07 - ms - Created
 */
@Relation
public class UserToGroup
{
  @Left
  User userId;
  
  @Right
  Group groupId;
}
