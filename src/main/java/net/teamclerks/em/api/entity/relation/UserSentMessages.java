package net.teamclerks.em.api.entity.relation;

import net.teamclerks.em.api.entity.*;
import net.teamclerks.em.auth.entity.*;

import com.techempower.data.*;
import com.techempower.data.annotation.*;

@Relation
public class UserSentMessages implements EntityRelationDescriptor<User, Sent>
{
  @Left
  User user;
  
  @Right
  Sent sent;
}
