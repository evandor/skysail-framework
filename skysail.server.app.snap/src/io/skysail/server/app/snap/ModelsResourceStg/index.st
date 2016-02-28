index(user, messages, converter, model) ::= <<
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Snap.svg</title>
<script type="text/javascript" src="/static/adobe/snap/0.4.1/snap.svg.js"></script>
<link href='http://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet' type='text/css'>
<script type="text/javascript" src="/static/adobe/snap/0.4.1/toDataUrl.js"></script>
<style type="text/css">
#svg {
  border: 2px solid #ccc;
  border-radius: 10px;
  float: left;
  font: 1em source-sans-pro, Source Sans Pro, Helvetica, sans-serif;
  height: 300px;
  margin-right: 10px;
  width: 1200px;
}
</style>
</head>
<body>
  <svg id="svg"></svg>
  <script type="text/javascript">
    var baseline = 240;
    var fontsize = 230;
    var fontfamily = 'Montserrat'

    var s = Snap("#svg");
    var bigCircle = s.circle(150, 150, 120);
    bigCircle.attr({
        fill: "#000066",
        stroke: "#000",
        strokeWidth: 5
    });
    var frontSail = s.path("M140 "+(baseline-20)+" l80 -10 Q 200 100 130 40 Q 140 150 140 " + (baseline-20));
    frontSail.attr({
        fill: "white",
        stroke: "#000",
        strokeWidth: 5
    });
    var backSail = s.path("M85 "+(baseline-20)+" l47 -5 L128 80 Q 140 150 85 " + (baseline-20));
    backSail.attr({
        fill: "#ccc",
        stroke: "#000",
        strokeWidth: 5
    });
    var hull = s.polygon(80, baseline-10, 85, baseline+5, 215,baseline-5, 230,baseline-25);
    hull.attr({
        fill: "#ccc",
        stroke: "black",
        strokeWidth: 5
    });
    
    var sky = s.text(320, baseline, "Sky");
    sky.attr({
        'font-size': fontsize,
        fontFamily: fontfamily,
        //fontWeight: 'bold',
        fill: "#ccc"
    });
    var sail = s.text(730, baseline, "sail");
    sail.attr({
        'font-size': fontsize,
        fontFamily: fontfamily,
        fill: "#000066"
    });
    
    var download = document.getElementById("svg");

    download.toDataURL("image/png", {
        callback : function(data) {
            // Convert image to 'octet-stream' (Just a download, really)
            var image = data.replace("image/png", "image/octet-stream");
            window.location.href = image;
        }
    });
  </script>

</body>
</html>

>>
