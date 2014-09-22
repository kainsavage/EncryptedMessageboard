em.provide("em.password.reset.process");

em.password.reset.process = (function() {
  "use strict";
  
  var renderData = {
      url: [ ],
      pageData: {
        headline: "Reset Your Password",
        un: $.getParameterByName("un"),
        vt: $.getParameterByName("vt")
      },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/password-reset-process.html"
        },
        {
          name: "errors",
          url: "/partials/password-reset/errors.html"
        }
      ],
      template: "/template/base.html"
  };
  
  function afterRender() {
    var form = $("#password-reset-process-form");

    window.document.title += " - Reset Your Password";
    
    form.on('submit', function(e) {
      e.preventDefault();
      
      $.ajax({
        type: "POST",
        url: "/api/password/process",
        data: form.serialize(),
        success: function(data) {
          if (data.success) {
            if (data.redirect) {
              window.location = data.redirect;
            }
            else {
              window.location = "/password-reset-complete";
            }
          }
          else {
            console.log(data);
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