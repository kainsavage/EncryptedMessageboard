/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.auth.entity;

import com.techempower.data.annotation.*;
import com.techempower.gemini.pyxis.*;
import com.techempower.util.*;

/**
 * Represents a user group for the EncryptedMessageboard application.
 *
 * @author (username)
 *
 * Development history:
 *   2013-10-07 - ms - Created
 */
@CachedEntity
public class Group
  implements PyxisUserGroup, Identifiable
{

  //
  // Member variables.
  //

  private int    id;
  private String name;               // 50 chars
  private String description;        // 100 chars
  private int    type;

  //
  // Member methods.
  //

  @Override
  public int getId()
  {
    return this.id;
  }

  @Override
  public void setId(int id)
  {
    this.id = id;
  }
  
  /**
   * Alias for getId(), required by PyxisUserGroup.
   */
  @Override
  public int getGroupID()
  {
    return getId();
  }

  /**
   * Alias for setId(int), required by PyxisUserGroup.
   */
  @Override
  public void setGroupID(int groupID)
  {
    setId(groupID);
  }

  /**
   * @see com.techempower.gemini.pyxisPyxisUserGroup#getDescription()
   */
  @Override
  public String getDescription()
  {
    return this.description;
  }

  /**
   * @see com.techempower.gemini.pyxisPyxisUserGroup#setDescription(String)
   */
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * @see com.techempower.gemini.pyxisPyxisUserGroup#getName()
   */
  @Override
  public String getName()
  {
    return this.name;
  }

  /**
   * @see com.techempower.gemini.pyxisPyxisUserGroup#setName(String)
   */
  @Override
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * @see com.techempower.gemini.pyxisPyxisUserGroup#getType()
   */
  @Override
  public int getType()
  {
    return this.type;
  }

  /**
   * @see com.techempower.gemini.pyxisPyxisUserGroup#setType(int)
   */
  @Override
  public void setType(int type)
  {
    this.type = type;
  }

}   // End Group.
