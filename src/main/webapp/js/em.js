var em = em || {};

/**
 * Creates the given namespace if it does not already exist.  For example: 
 *   em.provide('foo.bar.baz');
 *   
 * is equivalent to:
 *   foo = foo || {};
 *   foo.bar = foo.bar || {};
 *   foo.bar.baz = foo.bar.baz || {};
 * 
 * So the following code would execute safely:
 *   em.provide('foo.bar.baz');
 *   foo.bar.baz.count = 0;
 * 
 * @param namespace The namespace to be created.
 */
em.provide = function(namespace) {
  namespace = namespace.split('.');
  var object = window;
  for (var i = 0; i < namespace.length; i++) {
    object[namespace[i]] = object[namespace[i]] || {};
    object = object[namespace[i]];
  }
}

/**
 * Helper method for requiring users to be logged in.
 */
em.membersOnly = function(data) {
  if(data === null || data === undefined ||
     data.currentUser === null || data.currentUser === undefined) {
    window.location = "/";
  }
}

// Set a globally-scoped helper method
$.getParameterByName = function(name) {
  name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
      results = regex.exec(location.search);
  return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

// On DOM load, we need to load some scripts.
$(function() {
  // The path that we are going to route
  var path = window.location.pathname.substring(1),
      obj = em,
      exploded = null;
  
  // If there is no path (i.e. "/"), use 'home'.
  if (path.length == 0) {
    path = "home";
  }
  
  // Dynamically load this path's script OR a 404 if it does not exist.
  //$.getScript('/js/em.' + path + '.js')
  // .fail(function(jqxhr, settings, exception) {
  //   $.getScript('/js/em.404.js');
  // });
  
  // Make sure to replace odd chars with friendly ones
  exploded = path.replace(/\//g, '.').replace(/\-/g,'.').split('.');
  
  for(var i = 0; i < exploded.length; i++) {
    if ($.isPlainObject(obj[exploded[i]])) {
      obj = obj[exploded[i]];
    }
  }
  
  // Dynamically check to see what the "path" is that we are requesting,
  // and if there is a globally defined object at "em[path]"
  // and it has an 'init' function, then fire the init function.
  if($.isPlainObject(obj) && $.isFunction(obj.init)) {
    obj.init();
  } else {
    em.fourzerofour.init();
  }
});