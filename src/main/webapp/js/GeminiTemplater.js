GeminiTemplater = (function($) {
  'use strict';
  var templates = {},
      inProgress = {},
      queue = {},
      partialsQueue = 0,
      errorCallback,
      qualifyFileCallback,
      errorPartials = {},
      partialsInError = [],
      versioningEnabled = true,
      version = "2.0.26";
 
  /**
   * If either a request specific or global error handler function has been
   * defined, call it with the filename.
   *
   * @param {!string} fileName
   * @param {function(string)=} requestErrorCallback
   * @param {!object} response
   */
  function runErrorCallback(fileName, requestErrorCallback, response) {
    if ($.isFunction(requestErrorCallback) === true) {
      requestErrorCallback(fileName, response);
    } else if ($.isFunction(errorCallback) === true) {
      errorCallback(fileName);
    }
  };
 
  /**
   * If a template is currently being loaded, this adds a callback to the queue
   * of callbacks to be called once the template is loaded.
   *
   * @param fileName
   *          the template that is currently being loaded.
   * @param callback
   *          the callback function to call with this template once the template
   *          is loaded.
   */
  function pushToQueue(fileName, callback) {
    if (queue[fileName] === undefined) {
      queue[fileName] = [];
    }
    queue[fileName].push(callback);
  };
 
  /**
   * This should be called once a template has been successfully loaded.
   *
   * <p>
   * This processes all callbacks for this template and clears them from the
   * queue.
   *
   * <p>
   * If the template has not been loaded when this is called, the queue will
   * remain untouched.
   */
  function processQueue(fileName) {
    var currentQueue;
    if (queue[fileName] !== undefined) {
      currentQueue = queue[fileName];
      queue[fileName] = [];
      $.each(currentQueue, function() {
        var callback = this;
        getTemplate(fileName, callback);
      });
    }
  };
 
  /**
   * If a qualifyFileCallback function has been defined, calls that to get a
   * qualified and/or versioned template url.
   */
  function getTemplateUrl(fileName) {
    if ($.isFunction(qualifyFileCallback) === true) {
      return qualifyFileCallback(fileName);
    }
    return fileName + getVersionString();
  };
 
  /**
   * This function gets a template and passes it to the callback function
   * parameter. Multiple calls to this function for the same template will
   * result in only one ajax request for the template file. Multiple calls are
   * put into a queue until the template is loaded.
   *
   * @param {!string} fileName
   * @param {!function(Object)} callback
   * @param {function(string)=} failure
   *          If the ajax request to load the template failed, this callback
   *          function is called. If there is one request in progress and
   *          multiple requests queued, only the request that actually performs
   *          the ajax request triggers the failure function.
   */
  function getTemplate(fileName, callback, failure) {
    var skipPartialsCheck = (arguments[3] === true);
    if (templates[fileName] !== undefined) {
      callback(templates[fileName]);
      return;
    }
    if (partialsQueue > 0 && skipPartialsCheck !== true) {
      pushToQueue(fileName, callback);
      return;
    }
    // Template loading in progress, so push to queue
    if (inProgress[fileName] === true) {
      pushToQueue(fileName, callback);
      return;
    }
    // Request to load the template for a first time, so we'll perform an
    // ajax request to grab the template.
    inProgress[fileName] = true;
    $.ajax({
      url : getTemplateUrl(fileName),
      cache : true,
      timeout : 5000,
      tryCount : 0,
      retryLimit : 10,
      success : function(data) {
        if (templates[fileName] === undefined) {
          templates[fileName] = Handlebars.compile(data);
        }
        inProgress[fileName] = false;
        callback(templates[fileName]);
        // Process all the queued callbacks for this template now that the
        // template is loaded.
        processQueue(fileName);
      },
      // On error, this request is retried a set number of times.
      error : function(response) {
        // jquery bug workaround pre jquery 1.5
        // (http://bugs.jquery.com/ticket/7461)
        this.context = undefined;
 
        this.tryCount = this.tryCount + 1;
        // Double the timeout each time to account for slow connections
        this.timeout *= 2;
        // We'll retry this request tryCount times
        if (this.tryCount <= this.retryLimit) {
          $.ajax(this);
          return;
        }
        // Ok, looks like we couldn't load the template, despite multiple
        // tries. Call the error callback function.
        runErrorCallback(fileName, failure, response);
        inProgress[fileName] = false;
        return;
      }
    });
  };
 
  /**
   * If there is an error loading a template, this function will be called. The
   * callback function will be passed in the template file name as its first
   * argument.
   *
   * <p>
   * Note that this is overridden if an error callback function is passed to
   * getTemplate. This is basically the default error handler function.
   *
   * <p>
   * Passing in undefined, null, etc will cause a template loading error to
   * basically be silently ignored.
   */
  function setErrorCallbackFunction(callback) {
    errorCallback = callback;
  };
 
  /**
   * This function will be called before performing an ajax request for a
   * template. This function should generally take a relative path/file name and
   * make it absolute (by prepending something like '/images/' for example).
   * This might also be responsible for appending a versioning string.
   *
   * <p>
   * If this is not defined, you will need to pass in fully qualified file names
   * to the getTemplate function.
   *
   * @param {function(string)} callback
   */
  function setQualifyFileCallbackFunction(callback) {
    qualifyFileCallback = callback;
  };
  
  /**
   * A simple method for making a number of registerPartial calls. This 
   * function takes in an object with keys name, url, and optional fallback,
   * or an array of objects with the same hierarchy.
   * 
   * Example:
   * var partials = {
   *   name : "foo",
   *   url : "/foo.html",
   *   fallback : function() {}
   * };
   * 
   * var partials2 = [
   *   {
   *     name : "foo2",
   *     url : "/foo2.html",
   *     fallback : function() {}
   *   },
   *   {
   *     name : "foo3",
   *     url : "/foo3.html"
   *   }
   * ];
   * 
   * @param {Object} partials
   */
  function registerPartials(partials) {
    var partialCount = 0;
    
    if(partials !== undefined && partials !== null) {
      if($.isPlainObject(partials)) {
        // Make partials into an array of itself.
        partials = [partials];
      }
      
      for(partialCount; partialCount < partials.length; partialCount++) {
        registerPartial(partials[partialCount].name, partials[partialCount].url, partials[partialCount].fallback);
      }
    }
  };
 
  /**
   * Since templates are loaded asynchronously, you must use this to register a
   * partial instead of directly through Handlebars.
   * 
   * Unless failure is specified, a default failure implementation is run on a
   * failed attempted to get the template. This will try and use one of the 
   * errorPartials based on the status code of the response. It is possible to
   * set the errorPartials and expect this to function correctly on a given
   * response status code.
   * 
   * Example:
   * GeminiTemplater.setErrorPartials({404: "/404.html"});
   * GeminiTemplater.registerPartial("content", "/content.html");
   * 
   * This will cause a response with status code 404 for "/content.html" to
   * register "/404.html" as "content" instead. If "/content.html" returns
   * with a 200 status code, then it will register the partial normally.
   *
   * @param {!string} name
   * @param {!string} fileName
   * @param {function(string)=} failure
   */
  function registerPartial(name, fileName, failure) {
    var failCallback = function(aFilename, response) {
      if (errorPartials[response.status] !== undefined) {
        getTemplate(errorPartials[response.status], function(template) {
          partialsInError[name] = true;
          Handlebars.registerPartial(name, template);
          partialsQueue -= 1;
          if (partialsQueue === 0) {
            $.each(queue, function(key) {
              processQueue(key);
            });
          }
        }, null, true);
      }
    };
    
    partialsQueue += 1;
    
    if (failure === undefined || failure === null) {
      // This is fine, our default failCallback will work.
    }
    else if(!$.isFunction(failure)) {
      // Failure is provided but not a function; let's assume it's a string
      // we we want to use it as a failure-partial other than our error codes.
      failCallback = function(aFilename, response) {
        getTemplate(failure, function(template) {
          partialsInError[name] = true;
          Handlebars.registerPartial(name, template);
          partialsQueue -= 1;
          if (partialsQueue === 0) {
            $.each(queue, function(key) {
              processQueue(key);
            });
          }
        }, failCallback, true);
      };
    }
    else {
      // Failure is provided and a function; assume the dev knows what he/she
      // is doing and just assign it.
      failCallback = failure;
    }
    
    getTemplate(fileName, function(template) {
      Handlebars.registerPartial(name, template);
      partialsQueue -= 1;
      if (partialsQueue === 0) {
        $.each(queue, function(key) {
          processQueue(key);
        });
      }
    }, failCallback, true);
  };
  
  /**
   * Returns true iff the name of the partial corresponds to a partial template
   * that was found to be in error at the time of registration.
   */
  function isPartialInError(name) {
    return partialsInError[name] !== undefined && 
           partialsInError[name] !== null && 
           partialsInError[name];
  };
  
  /**
   * Sets the error partial for the given error status code.
   * 
   * Ex. GeminiTemplater.setErrorPartial(404, "/partials/404.html");
   */
  function setErrorPartial(errorStatusCode, errorPartial) {
    errorPartials[errorStatusCode] = errorPartial;
  };
  
  /**
   * Takes an arbitrary number of arguments and attempts to create a payload
   * for template rendering from them.
   * 
   * Used variables on the data object: 
   *   * url 
   *     A string, object, or array of strings/objects representing the url(s)
   *     from which data will be requested.
   *   * pageData 
   *     A plain object with variables that are to be included in the payload
   *     passed to the callback function.
   *   * partials
   *     A string, object, or array of strings/objects representing the partial
   *     templates to be passed to the callback (presumably, but not 
   *     necessarily for rendering).
   *     
   * @param {!object} data
   * @param {!function(Object)} callback
   * @param {!function(Object)} afterRender
   * @param {!function()} error 
   */
  function prepare(data, callback, afterRender, error) {
    var urlIndex = 0,
        ajaxCalls = [],
        payload = {},
        dfdArr = [],
        i = 0, c = 0,
        results = [];
  
    // Deal with data.url first.
    if(data.url !== undefined && data.url !== null && data.url !== '') {
      // It is definitely a value
      if(!$.isArray(data.url)) {
        // Then it is either a string or a plain object;
        // make it into an array of just itself.
        data.url = [data.url];
      }
      for(urlIndex; urlIndex < data.url.length; urlIndex++) {
        if(data.url[urlIndex] !== undefined && 
           data.url[urlIndex] !== null && 
           data.url[urlIndex] !== '') {
          // By here, data.url[urlIndex] is either a string or a plain object
          // which we assume can be passed to $.ajax().
          ajaxCalls[ajaxCalls.length] = data.url[urlIndex];
        }
      }
    }
    
    // Page data to be added as part of the payload.
    if ($.isPlainObject(data.pageData)) {
      payload = $.extend({}, payload, data.pageData);
    }

    if(data.partials === undefined || data.partials === null) {
      data.partials = [];
    }
    
    // Partials to be added as part of the payload.
    if ($.isArray(data.partials)) {
      payload = $.extend({}, payload, {partials:data.partials});
    }
    
    // We require a base template be defined on data
    payload = $.extend({}, payload, {template:data.template});
    
    if ($.isPlainObject(data.errorData)) {
      payload = $.extend({}, payload, {errorData:data.errorData});
    }
    
    // Add on the dependencies call.
    if ($.isFunction(data.dependencies)) {
      payload = $.extend({}, payload, {dependencies:data.dependencies})
    }
    
    // Time to start the magic; push each ajax call onto the deferred array.
    while (i < ajaxCalls.length) {
      // Yes, we are actually calling $.ajax here and putting the return in dfdArr.
      dfdArr.push($.ajax(ajaxCalls[i]));
      i ++;
    }
    
    // Once all the ajax calls are done, the promise is that our callback to 
    // .done() will be called.
    $.when.apply(null, dfdArr)
     .done(function() {
       ajaxFinished(arguments, payload, callback, afterRender, error)
     })
    .fail(function(e) {
      ajaxFinished(arguments, payload.errorData, callback, afterRender, error);
      // If ANY error occurs, just call the error callback.
      if ($.isFunction(error)) {
        error(e);
      }
    });
  };
  
  function ajaxFinished(args, payload, callback, afterRender, error) {
    var data = {},
        dataCount = 0;
    
    // In the case of multiple ajax calls, we will get an array of payloads.
    if($.isArray(args[0])) {
      for(dataCount; dataCount < args.length; dataCount++) {
        // Append each ajax payload to the overall data to be returned.
        data = $.extend({}, data, args[dataCount][0]);
      }
      // Finalize the payload.
      payload = $.extend({}, payload, data);
      // Dependencies to be called as the next-to-last step.
      if ($.isFunction(payload.dependencies)) {
        payload.dependencies(payload);
      }
      if ($.isFunction(callback)) {
        // By here, data is the ajax payloads in a single object, we need to
        // add the original payload data, and pass it to the callback.
        callback(payload, afterRender, error);
      }
    }
    // In the case of a single ajax call, the payload will just be the data.
    else {
      // Finalize the payload.
      payload = $.extend({}, payload, arguments[0][0]);
      // Dependencies to be called as the next-to-last step.
      if ($.isFunction(payload.dependencies)) {
        payload.dependencies(payload);
      }
      if ($.isFunction(callback)) {
        // By here, data is the ajax payloads in a single object, we need to
        // add the original payload data, and pass it to the callback.
        callback(payload, afterRender, error);
      }
    }
  };
  
  /**
   * Renders a template given data and calls afterRendering when finished.
   * If any errors occur, error will be called.
   * 
   * Note: it is expected (but not required) that data is the payload
   * generated via <code>prepare</code>.
   * 
   * @param {!object} data
   * @param {!function(Object)} afterRender
   * @param {!function()} error 
   */
  function render(data, afterRendering, error) {
    var partialCount = 0,
        partial,
        templateBody = 'body';
    
    if(data.partials === undefined || data.partials === null) {
      data.partials = [];
    }

    if(!$.isArray(data.partials)) {
      data.partials = [data.partials];
    }
    
    for(partialCount; partialCount < data.partials.length; partialCount++) {
      if ($.type(data.partials[partialCount]) === 'string') {
        partial = partials[data.partials[partialCount]];
        if(partial !== undefined && partial !== null) {
          GeminiTemplater.registerPartials({
            name: data.partials[partialCount],
            url: partial
          });
        }
      }
      else if ($.isPlainObject(data.partials[partialCount])) {
        GeminiTemplater.registerPartials(data.partials[partialCount]);
      }
    }
    
    if(data.templateBody !== undefined && data.templateBody !== null) {
      templateBody = data.templateBody;
    }
    
    GeminiTemplater.getTemplate(data.template, function(compiledTemplate) {
      $.when( $(templateBody).html(compiledTemplate(data)) )
       .then( afterRendering );
    });
  };
  
  /**
   * Method for getting the version string for partial
   */
  function getVersionString() {
    if(versioningEnabled) {
        return "?v=" + version;
    }
    
    return "";
  };
  
  /**
   * Initializes all the required defaults for the GeminiTemplater.
   */
  function init() {
    var errorIndex = 400;
    // This is sort of a disengenuous way of handling errors, but we can take
    // for granted that 400-600 are status codes for error (4** being a
    // client-error and 5** being a server error).
    
    // Don't cache any ajax requests or IE will render templates improperly.
    $.ajaxSetup({cache: false});
    
    // Gives access to these helper conditionals.
    Handlebars.registerHelper('ifCond', function (v1, operator, v2, options) {
      switch (operator) {
        case '==':
          return (v1 == v2) ? options.fn(this) : options.inverse(this);
          break;
        case '===':
          return (v1 === v2) ? options.fn(this) : options.inverse(this);
          break;
        case '!=':
          return (v1 != v2) ? options.fn(this) : options.inverse(this);
          break;
        case '!==':
          return (v1 !== v2) ? options.fn(this) : options.inverse(this);
          break;
        case '<':
          return (v1 < v2) ? options.fn(this) : options.inverse(this);
          break;
        case '<=':
          return (v1 <= v2) ? options.fn(this) : options.inverse(this);
          break;
        case '>':
          return (v1 > v2) ? options.fn(this) : options.inverse(this);
          break;
        case '>=':
          return (v1 >= v2) ? options.fn(this) : options.inverse(this);
          break;
        default:
          break;
      }
    });
    
    // Template helper for determining if all of the passed arguments are
    // null/undefined values. 
    Handlebars.registerHelper('ifNone', function() {
      var i = 0,
          any = false,
          options = arguments[arguments.length - 1];
      
      for(i; i < arguments.length - 1; i++) {
        if(arguments[i] !== undefined && arguments[i] !== null && arguments[i] !== '') {
          any = true;
        }
      }

      if(any) {
        return options.inverse(this);
      }
      else {
        return options.fn(this);
      }
    });
    
    // Template helper for determining if all of the passed arguments are
    // null/undefined values. 
    Handlebars.registerHelper('ifAny', function() {
      var i = 0,
          any = false,
          options = arguments[arguments.length - 1];
      
      for(i; i < arguments.length - 1; i++) {
        if(arguments[i] !== undefined && arguments[i] !== null && arguments[i] !== '') {
          any = true;
        }
      }

      if(any) {
        return options.fn(this);
      }
      else {
        return options.inverse(this);
      }
    });
  };
  
  init();
 
  // Define which functions should be public
  return {
    getTemplate : getTemplate,
    setErrorCallbackFunction : setErrorCallbackFunction,
    setQualifyFileCallbackFunction : setQualifyFileCallbackFunction,
    registerPartials : registerPartials,
    registerPartial : registerPartial,
    setErrorPartial : setErrorPartial,
    isPartialInError : isPartialInError,
    prepare : prepare,
    render : render
  };
}(jQuery));