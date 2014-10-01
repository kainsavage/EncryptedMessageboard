em.provide("em.friends");

em.friends = (function() {
  "use strict";
  
  var renderData = {
      url: [
        "/api/user/auth",
        "/api/user/friends"
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
    em.membersOnly(data);

    em.crypto.init(data.currentUser.userUsername);
    
    GeminiTemplater.render(data, afterRender);
  }
  
  function init() {
    GeminiTemplater.prepare(renderData, render);
  };
  
  return {
    init: init
  };    
}());