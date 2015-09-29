package net.teamclerks.em.api.entity.relation;

import net.teamclerks.em.api.entity.*;
import net.teamclerks.em.auth.entity.*;

import com.techempower.data.*;
import com.techempower.data.annotation.*;

@Relation
public class UserReceivedMessages implements
    EntityRelationDescriptor<User, Received>
{
  @Left
  User user;
  
  @Right
  Received received;
}
