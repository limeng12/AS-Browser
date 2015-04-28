<!DOCTYPE html>
<html>
<head>

	<meta http-equiv="X-UA-Compatible" content="chrome=1">
	<link rel="stylesheet" href="lib/as.css" type="text/css">

<!--
    <script type="text/javascript" src="script/ASPlot.js"></script>
-->

</head>

<body>
<!-- the title part-->
<h2>Alternative splicing browser<a id="help" href="http://191.101.1.231/browserhome/help.html">help</a></h2>

<div id="mainPage">
	<div id="inputModule">

		<form name="form" action="visualPage.php" onsubmit="return validate_form(this)" method="post">

			<p id="serachTitle">
				Welcome!
			</p>
			<img src="lib/logo.jpg" id="logo" height="80px" width="140px">
			<input type="text" name="eventId" value="ENST00000369781@2" id="searchFild" spellcheck='false'/>

			<input type="submit" value="Run" id="submitQuery" onclick="submitQueryEvent();"/>

			<img src="lib/loading.gif" id="loadingGif" height="80px" width="80px">
			
			<hr></hr><hr></hr>
			<!--					
			<input type="submit" value="run" id="submitQuery" onclick="submitQuery();"/>

			onclick="ASPlot.submitQuery();	
			<button type="button" id="submitQuery" onclick="submitQuery();"></button>
			-->

			<p><span class="em">Example1:</span>chr17:7572927:7573008:-@chr17:7573927:7574033:-:Splice@chr17:7576853:7576926:-@chr17:7577019:7577155:-:Splice@chr17:7577499:7577608:-@chr17:7578177:7578289:-@chr17:7578371:7578544:-@chr17:7579312:7579590:-@chr17:7579700:7579721:-@chr17:7579839:7579912:-</p>

			<p><span class="em">Example2:</span>ENST00000413465@2:3</p>
			<p><span class="em">Example3(miso):</span>chr16:71803526:71803602:-@chr16:71801780:71801788:-@chr16:71799392:71799487:-</p>

			<p><span class="em">Contact:331060295@qq.com</span></p>
		</form>

	</div>

<!--
	<hr></hr>
	<div id="visualASModule">
		<p>Exons structure</p>
		<canvas id="visualas" width="900" height="80">

	</div>

	<hr></hr>

	<div id="exonsModule">
		<p>Transcripts structure</p>
		<canvas id="visualtranscripts" width="900" height="200">

	</div>
-->

</div>

	<script type="text/javascript" src="lib/ASPlot.js">
		var loadingEle = document.getElementById("loadingGif");
		loadingEle.style.display = "none";
	</script>
	
</body>


</html>
