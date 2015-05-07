package net.teamclerks.em.api.collector;

import java.util.*;
import java.util.stream.*;

public class EMCollectors <T>
{
  /**
   * Static Collector used for returning the first (hopefully, only)
   * element in a stream, or null if empty.
   * 
   * @return The first element in the stream or null.
   */
  public static <T> Collector<T, List<T>, T> firstOrNull()
  {
    return Collectors.collectingAndThen(
      Collectors.toList(),
      list -> {
        if (list.size() == 0)
        {
          return null;
        }
        return list.get(0);
      }
    );
  }
}
