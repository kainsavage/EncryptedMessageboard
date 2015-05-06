var ObjectView = (function() {

  var url;

  function callMethod(methodName) {
    return function(evt) {
      evt.preventDefault();
      $.ajax({
        url : url + "&m=" + methodName,
        dataType: "json",
        success: function(data) {
          $("td#mv" + methodName).text(data.r);
        },
        error: function(req, status, error) {
          $("td#mv" + methodName).text("-- Error --");
        }
      });
    }
  }

  function callAllMethods(evt) {
    evt.preventDefault();
    $.ajax({
      url : url + "&m=all",
      dataType: "json",
      success: function(data) {
        result = data.r
        for (var key in result) {
          $("td#mv" + key).text(result[key]);
        }
      },
      error: function(req, status, error) {
        $("td#mv" + key).text("-- Error --");
      }
    });
  }

  function init(callUrl, fields, methods) {
    var content = [],
        key;

    url = callUrl;

    // Render object fields.
    for (key in fields) {
      content.push(e("tr", [
        e("td.admconfitem", [ key ]),
        e("td.admconfvalue", [ fields[key] ])
      ]));
    }
    $("#fields").html(content);

    // Render object get methods, with links to run each.
    content = [];
    for (i = 0; i < methods.length; i++) {
      content.push(e("tr", [
        e("td.admconfitem", [ methods[i] ]),
        e("td.admconfvalue", [
          e("span.anchor", { click: callMethod(methods[i]) }, [ "Invoke" ])
        ]),
        e("td#mv" + methods[i] + ".admconfvalue", [ "--" ])
      ]));
    }
    $("#methods").html(content);

    // Wire up "call all" get methods links.
    $(".object-view-call-all").click(callAllMethods);
  }

  return {
    "init": init
  };

})();