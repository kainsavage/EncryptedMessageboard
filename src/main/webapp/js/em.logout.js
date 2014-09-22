em.provide("em.logout");

em.logout = (function() {
  "use strict";
  
  var renderData = {
      url: [
       "/api/user/logout"
      ],
      pageData: {
        headline: "Logout"
      },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/logout.html" // Doesn't matter
        }
      ],
      template: "/template/base.html"
  };
  
  function afterRender() {
    window.document.title += " - Logout";
  }
  
  function init() {
    GeminiTemplater.prepare(renderData, GeminiTemplater.render, afterRender);
  };
  
  return {
    init: init
  };
}());