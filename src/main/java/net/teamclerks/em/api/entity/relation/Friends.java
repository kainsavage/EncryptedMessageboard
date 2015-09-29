/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */
 
package net.teamclerks.em.api.entity.relation;

import net.teamclerks.em.auth.entity.*;

import com.techempower.data.*;
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
public class Friends implements EntityRelationDescriptor<User,User>
{
  @Left
  User userA;
  
  @Right
  User userB;
}
