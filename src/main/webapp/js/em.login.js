em.provide("em.login");

em.login = (function() {
  "use strict";
  
  var renderData = {
      url: [
        "/api/user/auth"
      ],
      pageData: {
        loginPage: true,
        noKey: true,
        headline: "Login"
      },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/login.html"
        },
        {
          name: "errors",
          url: "/partials/login/errors.html"
        }
      ],
      template: '/template/base.html'
  };
  
  function afterRender() {
    var form = $("#login-form");
    
    window.document.title += " - Login";
    
    form.on('submit', function(e) {
      e.preventDefault();
      
      $.ajax({
        type: "POST",
        url: "/api/user/login",
        data: form.serialize(),
        success: function(data) {
          if (data.success) {
            if (data.redirect) {
              window.location = data.redirect;
            }
            else {
              window.location = "/home";
            }
          }
          else {
            $("#errors").html(Handlebars.partials['errors'](data));
          }
        }
      });
    });
  }
  
  function init() {
    GeminiTemplater.prepare(renderData, GeminiTemplater.render, afterRender);
  };
  
  return {
    init: init
  };    
}());