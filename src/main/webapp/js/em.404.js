em.provide("em.fourzerofour");

em.fourzerofour = (function() {
  "use strict";
  
  var renderData = {
      url: [
        "/api/user/auth"
      ],
      pageData: { },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/404.html"
        }
      ],
      template: '/template/base.html'
  };
  
  function afterRender() {
    document.title = "404";
  };
  
  function render(data) {
    data.keyExists = em.crypto.keyExists();
    
    GeminiTemplater.render(data, afterRender);
  }
  
  function init() {
    GeminiTemplater.prepare(renderData, render);
  };
  
  return {
    init: init
  };    
}());