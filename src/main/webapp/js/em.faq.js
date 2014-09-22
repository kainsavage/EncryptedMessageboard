em.provide("em.faq");

em.faq = (function() {
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
          url: "/partials/faq.html"
        }
      ],
      template: '/template/base.html'
  };
  
  function afterRender() {
    window.document.title += " - FAQ";
  };
  
  function render(data) {
    // Check for the key
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