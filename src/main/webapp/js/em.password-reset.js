em.provide("em.password.reset");

em.password.reset = (function() {
  "use strict";
  
  var renderData = {
      url: [],
      pageData: {
        loginPage: true,
        headline: "Password Reset"
      },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/password-reset.html"
        },
        {
          name: "errors",
          url: "/partials/password-reset/errors.html"
        }
      ],
      template: '/template/base.html'
  };
  
  function afterRender() {
    var form = $("#password-reset-form");
    
    window.document.title += " - Password Reset";
    
    form.on('submit', function(e) {
      e.preventDefault();
      
      $.ajax({
        type: "POST",
        url: "/api/password/reset",
        data: form.serialize(),
        success: function(data) {
          if (data.success) {
            if (data.redirect) {
              window.location = data.redirect;
            }
            else {
              window.location = "/password-reset-confirmed";
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