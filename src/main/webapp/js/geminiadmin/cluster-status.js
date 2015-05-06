$(function() {

  function render(nodes, thisnode) {
    var c = "",
        i;
  
    nodes.sort(nodesById);
    for (i in nodes) {
      c += "<tr" + (thisnode == nodes[i].id ? " class='thisnode'" : "") + "><td class='numeric'>" + nodes[i].id + "</td>"
         + "<td>" + nodes[i].ch + "</td>"
         + "<td>" + nodes[i].name + "</td>"
         + "<td>" + gba.standardDateString(new Date(nodes[i].ct)) + "</td>"
         + "<td>" + nodes[i].het + " (" + nodes[i].et + " ms)</td>"
         + "<td class='numeric'>" + nodes[i].mc + "</td>"
         + "<td>" + nodes[i].desc + "</td></tr>";
    }
  
    $("#nodes").html(c);
    $("#thisnode").text("This is node ID " + thisnode + ".");
  }
  
  function nodesById(n1, n2) {
    return n1.id - n2.id;
  }

  function refreshStatus() {
    $.ajax({
      url: "?mode=1",
      dataType: "json",
      type: "get",
      data: {},
      success: function(data) {
        render(data.nodes, data.thisnode);
      }
    });
  }

  refreshStatus();

});