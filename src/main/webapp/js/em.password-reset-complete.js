em.provide("em.password.reset.complete");

em.password.reset.complete = (function() {
  "use strict";
  
  var renderData = {
      url: [ ],
      pageData: {
        headline: "Password reset complete"
      },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/password-reset-complete.html"
        }
      ],
      template: "/template/base.html"
  };
  
  function afterRender() {
    window.document.title += " - Password Reset Complete";
  }
  
  function init() {
    GeminiTemplater.prepare(renderData, GeminiTemplater.render, afterRender);
  };
  
  return {
    init: init
  };    
}());