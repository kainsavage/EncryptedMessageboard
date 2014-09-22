em.provide("em.register");

em.register = (function() {
  "use strict";
  
  var renderData = {
      url: [
        "/api/user/auth"
      ],
      pageData: {
        registerPage: true,
        noKey : true
      },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/register.html"
        },
        {
          name: "errors",
          url: "/partials/login/errors.html"
        }
      ],
      template: '/template/base.html'
  };
  
  function afterRender() {
    var form = $("#register-form");
    
    window.document.title += " - Register";
    
    form.on('submit', function(e) {
      e.preventDefault();
      
      $.ajax({
        type: "POST",
        url: "/api/user/register",
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