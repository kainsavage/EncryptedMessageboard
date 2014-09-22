em.provide("em.home");

em.home = (function() {
  "use strict";
  
  var renderData = {
      url: [
        "/api/user/auth",
        "/api/message/latest"
      ],
      pageData: {
        homePage: true
      },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/home.html"
        }
      ],
      template: '/template/base.html'
  };
  
  function afterRender() {
    window.document.title += " - Home";
  };
  
  function render(data) {
    // Check for the key
    data.keyExists = em.crypto.keyExists();
    
    if(!data.keyExists) {
      window.location = "/profile";
    }
    
    GeminiTemplater.render(data, afterRender);
  }
  
  function init() {
    GeminiTemplater.prepare(renderData, render);
  };
  
  return {
    init: init
  };    
}());