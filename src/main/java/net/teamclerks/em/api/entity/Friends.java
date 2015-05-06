/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */
 
package net.teamclerks.em.api.entity;

import net.teamclerks.em.auth.entity.User;

import com.techempower.data.annotation.Left;
import com.techempower.data.annotation.Relation;
import com.techempower.data.annotation.Right;

/**
 * Represents a user group relation for the EncryptedMessageboard application.
 *
 * @author (username)
 *
 * Development history:
 *   2013-10-07 - ms - Created
 */
@Relation
public class Friends
{
  @Left
  User from;
  
  @Right
  User to;
}
