em.provide("em.profile");

em.profile = (function() {
  "use strict";
  
  var renderData = {
      url: [
        "/api/user/auth"
      ],
      pageData: {
        profilePage: true
      },
      errorData: { },
      dependencies: function(data) { },
      partials: [
        {
          name: "body",
          url: "/partials/profile.html"
        },
        {
          name: "errors",
          url: "/partials/profile/errors.html"
        },
        {
          name: "success",
          url: "/partials/profile/success.html"
        }
      ],
      template: '/template/base.html'
  },
  openModal = null;
  
  function attachTooltips() {
    $("#firstname").tooltip({
      // place tooltip on the right edge
      position: "center right",
      // a little tweaking of the position
      offset: [-15, -230],
      // use the built-in fadeIn/fadeOut effect
      effect: "fade",
      // custom opacity setting
      //opacity: 0.8,
      // use this single tooltip element
      tip: "#firstnametip"
    });
    
    $("#lastname").tooltip({
      // place tooltip on the right edge
      position: "center right",
      // a little tweaking of the position
      offset: [-15, -230],
      // use the built-in fadeIn/fadeOut effect
      effect: "fade",
      // custom opacity setting
      //opacity: 0.8,
      // use this single tooltip element
      tip: "#lastnametip"
    });
    
    $("#email").tooltip({
      // place tooltip on the right edge
      position: "center right",
      // a little tweaking of the position
      offset: [0, -230],
      // use the built-in fadeIn/fadeOut effect
      effect: "fade",
      // custom opacity setting
      //opacity: 0.8,
      // use this single tooltip element
      tip: "#emailtip"
    });
    
    $("#publicKey").tooltip({
      // place tooltip on the right edge
      position: "center right",
      // a little tweaking of the position
      offset: [0, -230],
      // use the built-in fadeIn/fadeOut effect
      effect: "fade",
      // custom opacity setting
      //opacity: 0.8,
      // use this single tooltip element
      tip: "#publicKeytip"
    });
    
    $("#privateKey").tooltip({
      // place tooltip on the right edge
      position: "center right",
      // a little tweaking of the position
      offset: [0, -230],
      // use the built-in fadeIn/fadeOut effect
      effect: "fade",
      // custom opacity setting
      //opacity: 0.8,
      // use this single tooltip element
      tip: "#privateKeytip"
    });
  }
  
  function generateKeypair() {
    setTimeout(function(){
      var key = em.crypto.generateKeypair(2048);
      if(openModal) {
        openModal.close();
      }
      $("#newKey").modal({});
      $("#publicKey").html(key.publicKeyArmored);
      $("#privateKey").html(key.privateKeyArmored);
    }, 100);
  }
  
  function attachGenerateKeypairControl() {
    $("#generate").on('click', function(e) {
      if(em.crypto.keyExists()) {
        openModal = $("#confirmOld").modal({});
      }
      else {
        openModal = $("#confirmNew").modal({});
      }
    });
  }
  
  function saveProfile(e) {
    var data = $(this).serialize().substring(0,$(this).serialize().indexOf("&private")),
        validKey = false;
    
    e.preventDefault();
    
    $("#success").html("");
    $("#errors").html("");

    // Check that the keys are valid and match.
    try {
      var emsg = em.crypto.encryptMessageWithPublicKeyArmored($("#publicKey").val().replace(/\r\n/g,"\n").replace(/\n/g,"\r\n"), "test");
      if(emsg.success) {
        var dmsg = em.crypto.decryptMessageWithPrivateKeyArmored($("#privateKey").val().replace(/\r\n/g,"\n").replace(/\n/g,"\r\n"), emsg.message);
        if(dmsg.success) {
          if(dmsg.message === "test") {
            validKey = true;
          }
        }
      }
    }
    catch(err) {};
    
    if(validKey) {
      $.ajax({
        type: "POST",
        url: "/api/profile/save",
        data: data,
        success: function(data) {
          if (data.success) {
            $("#success").html(Handlebars.partials['success'](data));
          }
          else {
            $("#errors").html(Handlebars.partials['errors'](data));
          }
        }
      });
    }
    else {
      data = {
        validation : {
          instructions : "Your public and private keys do not match."
        }  
      };
      $("#errors").html(Handlebars.partials['errors'](data));
    }
  }
    
  function afterRender() {
    window.document.title += " - Home";
    
    attachTooltips();
    
    attachGenerateKeypairControl();
    
    $(".doit").on('click', generateKeypair);
    
    $("form").on('submit', saveProfile);
  };
  
  function render(data) {
    // Check for the key
    data.keyExists = em.crypto.keyExists();
    
    if(!data.currentUser) {
      window.location = "/home";
    }
    
    if(data.keyExists) {
      data.privateKeyArmored = em.crypto.getKey().privateKeyArmored;
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