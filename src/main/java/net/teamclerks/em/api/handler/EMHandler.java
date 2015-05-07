package net.teamclerks.em.api.handler;

import net.teamclerks.em.*;

import com.techempower.gemini.path.*;
import com.techempower.js.*;

/**
 * Base class for type coercion.
 * 
 * @author kain
 */
public abstract class EMHandler extends MethodUriHandler<EMContext>
{
  public EMHandler(EMApplication app)
  {
    super(app);
  }
  
  public EMHandler(EMApplication app, String componentCode)
  {
    super(app,componentCode);
  }
  
  public EMHandler(EMApplication app, String componentCode, JavaScriptWriter jsw)
  {
    super(app,componentCode,jsw);
  }

  @Override
  public EMApplication app()
  {
    return (EMApplication)super.app();
  }
}
