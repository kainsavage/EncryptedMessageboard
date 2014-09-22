em.provide("em.friends");

em.friends = (function() {
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
          url: "/partials/friends.html"
        }
      ],
      template: '/template/base.html'
  };
  
  function afterRender() {
    window.document.title += " - Friends";
  };
  
  function render(data) {
    // Check for the key
    data.keyExists = em.crypto.keyExists();
    
    if(data.keyExists) {
      data.friends = em.crypto.listFriends();
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