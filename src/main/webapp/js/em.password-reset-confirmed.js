em.provide("em.password.reset.confirmed");

em.password.reset.confirmed = (function() {
  "use strict";
  
  var renderData = {
      url: [ ],
      pageData: {
        headline: "Password reset request confirmed"
      },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/password-reset-confirmed.html"
        }
      ],
      template: "/template/base.html"
  };
  
  function afterRender() {
    window.document.title += " - Password Reset Request Confirmation";
  }
  
  function init() {
    GeminiTemplater.prepare(renderData, GeminiTemplater.render, afterRender);
  };
  
  return {
    init: init
  };    
}());