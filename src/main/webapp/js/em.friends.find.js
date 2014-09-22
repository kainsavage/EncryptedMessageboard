em.provide("em.friends.find");

em.friends.find = (function() {
  "use strict";
  
  var renderData = {
      url: [
        "/api/user/auth"
      ],
      pageData: {
        friendsPage: true
      },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/friends/find.html"
        }
      ],
      template: '/template/base.html'
  };
  
  function afterRender() {
    window.document.title += " - Friends - Find";
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