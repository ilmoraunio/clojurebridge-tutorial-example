$(document).ready(function () {
  $.get("/foo").done(function (data) {
    $("#container").append(JSON.stringify(data.foo[1]));
  });
});
